package com.acme.flotte.rest;

import com.acme.flotte.entity.Flottenfahrzeug;
import com.acme.flotte.service.FlottenfahrzeugReadService;
import com.acme.flotte.service.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

import static com.acme.flotte.rest.FlottenfahrzeugGetController.REST_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Der RestController fur das Lesen.
 */
@RestController
@RequestMapping(REST_PATH)
@Tag(name = "Flottenfahrzeug API")
@RequiredArgsConstructor
@Slf4j
final class FlottenfahrzeugGetController {

    /**
     * Basispfad für die REST-Schnittstelle.
     */
    static final String REST_PATH = "/rest";
    /**
     * Muster für eine UUID. `$HEX_PATTERN{8}-($HEX_PATTERN{4}-){3}$HEX_PATTERN{12}` enthält eine _capturing group_
     * und ist nicht zulässig.
     */

    static final String ID_PATTERN =
        "[\\dA-Fa-f]{8}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{12}";

    private final FlottenfahrzeugReadService service;

    /**
     * Suche anhand der Flottenfahrzeug-ID als Pfad-Parameter.
     *
     * @param id ID des zu suchenden Flottenfahrzeugs
     * @return Ein Response mit dem Statuscode 200 und dem gefundenen Flottenfahrzeug mit oder Statuscode 404.
     */
    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Suche mit der Flottenfahrzeug-ID", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Flottenfahrzeug gefunden")
    @ApiResponse(responseCode = "404", description = "Flottenfahrzeug nicht gefunden")
    Flottenfahrzeug findById(@PathVariable final UUID id) {
        log.debug("findById: id={}", id);

        final var flottenfahrzeug = service.findById(id);
        log.debug("findById: {}", flottenfahrzeug);

        return flottenfahrzeug;



    }





    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Suche mit Suchkriterien", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "CollectionModel mid den Flottenfahrzeuge")
    @ApiResponse(responseCode = "404", description = "Keine Flottenfahrzeuge gefunden")
    CollectionModel<Flottenfahrzeug> find(
        @RequestParam final Map<String, String> suchkriterien

    ) {
        log.debug("find: suchkriterien={}", suchkriterien);
        final var flottenfahrzeuge = service.find(suchkriterien);
        log.debug("find: {}", flottenfahrzeuge);

        return CollectionModel.of(flottenfahrzeuge);
    }

    @ExceptionHandler
    @ResponseStatus
    void onNotFound(final NotFoundException ex) {
        log.debug("onNotFound: {}", ex.getMessage());

    }


}
