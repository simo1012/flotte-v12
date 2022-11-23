package com.acme.flotte.graphql;

import com.acme.flotte.entity.Flottenfahrzeug;
import com.acme.flotte.service.FlottenfahrzeugReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.UUID;

/**
 * Eine Controller-Klasse für das Lesen mit der GraphQL-Schnittstelle und den Typen aus dem GraphQL-Schema.
 *
 */
@Controller
@RequiredArgsConstructor
@Slf4j
final class FlottenfahrzeugQueryController {
    private final FlottenfahrzeugReadService service;

    /**
     * Suche anhand der Flotten-ID.
     *
     * @param id ID des zu suchenden Flottenfahrzeugs
     * @return Das gefundene Flottenfahrzeug.
     */

    @QueryMapping
    Flottenfahrzeug flottenfahrzeug(@Argument final UUID id) {
        log.debug("findById: id={}", id);
        final var flottenfahrzeug = service.findById(id);
        log.debug("findById: {}", flottenfahrzeug);
        return flottenfahrzeug;
    }

    /**
     * Suche mit diversen Suchkriterien.
     *
     * @param suchkriterien Suchkriterien und ihre Werte, z.B. `kennzeichen` und `Alpha`
     * @return Die gefundenen flottenfahrzeuge als Collection
     */
    @QueryMapping
    Collection<Flottenfahrzeug> flottenfahrzeuge(@Argument("input") final Suchkriterien suchkriterien){
        log.debug("find: suchkriterien={}", suchkriterien);
        final var flottenfahrzeuge = service.find(suchkriterien.toMap());
        log.debug("find: {}", flottenfahrzeuge);
        return flottenfahrzeuge;
    }
}
