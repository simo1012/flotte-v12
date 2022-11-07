package com.acme.flotte.rest;

import com.acme.flotte.entity.Flottenfahrzeug;
import com.acme.flotte.service.FlottenfahrzeugReadService;
import com.acme.flotte.service.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

/**
 * Der RestController fur das Lesen.
 */
@RestController
@RequestMapping("/")
@Tag(name = "Flottenfahrzeug API")
@RequiredArgsConstructor
@Slf4j
final class FlottenfahrzeugGetController {
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
     * @return Ein Response mit dem Statuscode 200 und dem gefundenen Flottenfahrzeug mit Atom-Links oder Statuscode 404.
     */
    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Suche mit der Flottenfahrzeug-ID", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Flottenfahrzeug gefunden")
    @ApiResponse(responseCode = "404", description = "Flottenfahrzeug nicht gefunden")
    ResponseEntity<Flottenfahrzeug> findById(@PathVariable final UUID id) {
        log.debug("findById: id={}", id);

        final var flottenfahrzeug = service.findById(id);
        log.debug("findById: {}", flottenfahrzeug);

        return ok(flottenfahrzeug);



    }



    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Suche Alle", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Liste mit den Flottenfahrzeuge")
    ResponseEntity<List<Flottenfahrzeug>> find() {
        log.debug("find:");

        final var flottenfahrzeuge = service.find();
        log.debug("find: {}", flottenfahrzeuge);

        return ok(flottenfahrzeuge);
    }

    @ExceptionHandler(NotFoundException.class)
    @SuppressWarnings("unused")
    ResponseEntity<Void> handleNotFound(final NotFoundException ex) {
        log.debug("handleNotFound: {}", ex.getMessage());
        return notFound().build();
    }















}
