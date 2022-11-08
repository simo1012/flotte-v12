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
import java.util.UUID;
import java.util.stream.Collectors;

import static com.acme.flotte.repository.DB.FLOTTENFAHRZEUGE;

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
     * @param kennzeichen Der (Teil-) Kennzeichen der gesuchten Kunden
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

}
