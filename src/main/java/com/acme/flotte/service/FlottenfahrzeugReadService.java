package com.acme.flotte.service;

import com.acme.flotte.entity.Flottenfahrzeug;
import com.acme.flotte.repository.FlottenfahrzeugRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
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
