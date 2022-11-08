package com.acme.flotte.service;

import com.acme.flotte.entity.Flottenfahrzeug;
import com.acme.flotte.repository.FlottenfahrzeugRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Anwendungslogik für Flottenfahrzeug.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public final class FlottenfahrzeugReadService {
    private final FlottenfahrzeugRepository repo;

    /**
     * Einen Flottenfahrzeug anhand seiner ID suchen.
     *
     * @param id Die Id des gesuchten Flottenfahrzeug
     * @return Der gefundene Flottenfahrzeug
     * @throws NotFoundException Falls kein Flottenfahrzeug gefunden wurde
     */
    public @NonNull Flottenfahrzeug findById(final UUID id) {
        log.debug("findById: id={}", id);
        final var flottenfahrzeug = repo.findById(id)
            .orElseThrow(() -> new NotFoundException(id));
        log.debug("findById: {}", flottenfahrzeug);
        return flottenfahrzeug;
    }

    /**
     * Flottenfahrzeug anhand von Suchkriterien als Collection suchen.
     *
     * @param suchkriterien Die Suchkriterien
     * @return Die gefundenen Flottenfahrzeug oder eine leere Liste
     * @throws NotFoundException Falls keine Flottenfahrzeuge gefunden wurden
     */
    @SuppressWarnings({"ReturnCount", "NestedIfDepth"})
    public Collection<Flottenfahrzeug> find(final Map<String, String> suchkriterien) {
        log.debug("find: suchkriterien={}", suchkriterien);

        if (suchkriterien.isEmpty()) {
            return repo.findAll();
        }

        if (suchkriterien.size() == 1) {
            final var kennzeichen = suchkriterien.get("kennzeichen");
            if (kennzeichen != null) {
                final var flottenfahrzeuge = repo.findByKennzeichen(kennzeichen);
                if (flottenfahrzeuge.isEmpty()) {
                    throw new NotFoundException(suchkriterien);
                }
                log.debug("find (kennzeichen): {}", kennzeichen);
                return flottenfahrzeuge;
            }

            final var email = suchkriterien.get("email");
            if (email != null) {
                final var flottenfahrzeug = repo.findByEmail(email);
                if (flottenfahrzeug.isEmpty()) {
                    throw new NotFoundException(suchkriterien);
                }
                final var flottenfahrzeuge = List.of(flottenfahrzeug.get());
                log.debug("find (email): {}", flottenfahrzeuge);
                return flottenfahrzeuge;
            }
        }
        final var flottenfahrzeuge = repo.find(suchkriterien);
        if (flottenfahrzeuge.isEmpty()) {
            throw new NotFoundException(suchkriterien);
        }
        log.debug("find: {}", flottenfahrzeuge);
        return flottenfahrzeuge;

    }

    /**
     * Alle Flottenfahrzeuge suchen.
     *
     * @return Die gefundenen Flottenfahrzeuge.
     */
    @SuppressWarnings({"ReturnCount", "NestedIfDepth"})
    public List<Flottenfahrzeug> find() {
        log.debug("find:");

        return repo.findAll();
    }
}
