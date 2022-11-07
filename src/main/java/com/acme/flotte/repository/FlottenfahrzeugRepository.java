package com.acme.flotte.repository;

import com.acme.flotte.entity.Flottenfahrzeug;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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
}
