package com.acme.flotte.repository;

import com.acme.flotte.entity.Flottenfahrzeug;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.acme.flotte.repository.DB.FLOTTENFAHRZEUGE;
import static java.util.UUID.randomUUID;

/**
 * Repository für den DB-Zugriff bei Flottenfahrzeug.
 */
@Repository
@Slf4j
@SuppressWarnings("PublicConstructor")
public final class FlottenfahrzeugRepository {
    /**
     * Ein Flottenfahrzeug anhand der Id suchen.
     *
     * @param id - Die id des Flottenfahrzeugs
     * @return das gefundene Flottenfahrzeug oder null
     */
    public Optional<Flottenfahrzeug> findById(final UUID id) {
        log.debug("findById: id={}", id);
        final var result = FLOTTENFAHRZEUGE.stream()
            .filter(flottenfahrzeug -> Objects.equals(flottenfahrzeug.getId(), id))
            .findFirst();
        log.debug("findById: {}", result);
        return result;

    }

    /**
     * Alle Flottenfahrzeuge als Liste ermitteln.
     *
     * @return All Flottenfahrzeuge
     */
    public @NonNull List<Flottenfahrzeug> findAll() {
        return FLOTTENFAHRZEUGE;
    }

    /**
     * Flottenfahrzeug anhand des Kennzeichen suchen.
     *
     * @param kennzeichen Der (Teil-) Kennzeichen der gesuchten flotten
     * @return Die gefundenen flottenfahrzeuge oder eine leere Collection
     */

    public @NonNull Collection<Flottenfahrzeug> findByKennzeichen(final CharSequence kennzeichen) {
        log.debug("findByKennzeichen: kennzeichen:={}", kennzeichen);
        final var flottenfahrzeuge = FLOTTENFAHRZEUGE.stream()
            .filter(flottenfahrzeug -> flottenfahrzeug.getKennzeichen().contains(kennzeichen))
            .collect(Collectors.toList());
        log.debug("findByKennzeichen: flottenfahrzeuge={}", flottenfahrzeuge);
        return flottenfahrzeuge;
    }

    /**
     * Flottenfahrzeug zu gegebener Emailadresse aus der DB ermitteln.
     *
     * @param email Emailadresse für die Suche
     * @return Gefundenes Flottenfahrzeug oder null
     */
    public Optional<Flottenfahrzeug> findByEmail(final String email) {
        log.debug("findByEmail: {}", email);
        final var result = FLOTTENFAHRZEUGE.stream()
            .filter(flottenfahrzeug -> Objects.equals(flottenfahrzeug.getEmail(), email))
            .findFirst();
        log.debug("findByEmail: {}", result);
        return result;
    }

    /**
     * Flottenfahrzeuge anhand von Suchkriterien ermitteln.
     * Z.B. mit GET https://localhost:8080/api?kennzeichen=mvv1337&amp;plz=68163
     *
     * @param suchkriterien Suchkriterien.
     * @return Gefundene Flottenfahrzeuge.
     */
    @SuppressWarnings({"ReturnCount", "JavadocLinkAsPlainText"})
    public @NonNull Collection<Flottenfahrzeug> find(final Map<String, String> suchkriterien) {
        log.debug("find: suchkriterien={}", suchkriterien);

        if (suchkriterien.isEmpty()) {
            return findAll();
        }

        for (final var entry : suchkriterien.entrySet()) {
            switch (entry.getKey()) {
                case "email" -> {
                    final var result = findByEmail(entry.getValue());
                    //noinspection OptionalIsPresent
                    return result.isPresent() ? List.of(result.get()) : Collections.emptyList();
                }
                case "kennzeichen" -> {
                    return findByKennzeichen(entry.getValue());
                }
                default -> log.debug("find: ungueltiges Suchkriterium={}", entry.getKey());
            }
        }
        return Collections.emptyList();
    }

    /**
     * Abfrage, ob es ein Flottenfahrzeug mit gegebener Emailadresse gibt.
     *
     * @param email Emailadresse für die Suche
     * @return true, falls es ein solches Flottenfahrzeug gibt, sonst false
     */
    public boolean isEmailExisting(final String email) {
        log.debug("isEmailExisting: email={}", email);
        final var count = FLOTTENFAHRZEUGE.stream()
            .filter(flottenfahrzeug -> Objects.equals(flottenfahrzeug.getEmail(), email))
            .count();
        log.debug("isEmailExisting: count={}", count);
        return count > 0L;
    }

    /**
     * Ein neues Flottenfahrzeug anlegen.
     *
     * @param flottenfahrzeug Das Objekt des neu anzulegenden Flottenfahrzeug.
     * @return Das neu angelegte Flottenfahrzeug mit generierter ID und kleingeschriebener Emailadresse
     */
    public @NonNull Flottenfahrzeug create(final @NonNull Flottenfahrzeug flottenfahrzeug) {
        log.debug("create: {}", flottenfahrzeug);
        flottenfahrzeug.setId(randomUUID());
        FLOTTENFAHRZEUGE.add(flottenfahrzeug);
        log.debug("create: {}", flottenfahrzeug);
        return flottenfahrzeug;
    }

    /**
     * Einen vorhandenes Flottenfahrzeug aktualisieren.
     *
     * @param flottenfahrzeug Das Objekt mit den neuen Daten
     */
    public void update(final @NonNull Flottenfahrzeug flottenfahrzeug) {
        log.debug("update: {}", flottenfahrzeug);
        final OptionalInt index = IntStream
            .range(0, FLOTTENFAHRZEUGE.size())
            .filter(i -> Objects.equals(FLOTTENFAHRZEUGE.get(i).getId(), flottenfahrzeug.getId()))
            .findFirst();
        log.trace("update: index={}", index);
        if (index.isEmpty()) {
            return;
        }
        FLOTTENFAHRZEUGE.set(index.getAsInt(), flottenfahrzeug);
        log.debug("update: {}", flottenfahrzeug);
    }


}
