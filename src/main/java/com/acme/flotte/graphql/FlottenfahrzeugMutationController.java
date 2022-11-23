package com.acme.flotte.graphql;

import com.acme.flotte.service.FlottenfahrzeugWriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

/**
 * Eine Controller-Klasse für das Schreiben mit der GraphQL-Schnittstelle und den Typen aus dem GraphQL-Schema.
 *
 */
@Controller
@RequiredArgsConstructor
@Slf4j
final class FlottenfahrzeugMutationController {
    private final FlottenfahrzeugWriteService service;

    /**
     * Ein neues Flottenfahrzeug anlegen.
     *
     * @param input Die Eingabedaten für einen neues Flottenfahrzeug
     * @return Die generierte ID für das neue Flottenfahrzeug als Payload
     */
    @MutationMapping
    CreatePayload create(@Argument final FlottenfahrzeugInput input){
        log.debug("create: input={}", input);
        final var id = service.create(input.toFlottenfahrzeug()).getId();
        log.debug("create: id={}", id);
        return new CreatePayload(id);
    }
}
