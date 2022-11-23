package com.acme.flotte.rest;

import com.acme.flotte.service.FlottenfahrzeugWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static com.acme.flotte.rest.FlottenfahrzeugGetController.ID_PATTERN;
import static com.acme.flotte.rest.FlottenfahrzeugGetController.REST_PATH;
import static com.acme.flotte.rest.UriHelper.getBaseUri;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;

/**
 * Eine `@RestController`-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
 * Funktionen der Klasse abgebildet werden.
 *
 */
@RestController
@RequestMapping(REST_PATH)
@Tag(name = "Flottenfahrzeug API")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("ClassFanOutComplexity")
public class FlottenfahrzeugWriteController {

    private final FlottenfahrzeugWriteService service;
    /**
     * Einen neuen Flotten-Datensatz anlegen.
     *
     * @param flottenfahrzeugDTO Das Flottenfahrzeugobjekt aus dem eingegangenen Request-Body.
     * @param request Das Request-Objekt, um `Location` im Response-Header zu erstellen.
     * @return Response mit Statuscode 201 einschließlich Location-Header oder Statuscode 422 falls Constraints verletzt
     *      sind oder die Emailadresse bereits existiert oder Statuscode 400 falls syntaktische Fehler im Request-Body
     *      vorliegen.
     * @throws URISyntaxException falls die URI im Request-Objekt nicht korrekt wäre
     */

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Ein neues Flottenfahrzeug anlegen", tags = "Neuanlegen")
    @ApiResponse(responseCode = "201", description = "Flottenfahrzeug neu angelegt")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte oder Email vorhanden")
    @SuppressWarnings("TrailingComment")
    ResponseEntity<Void> create(
        @RequestBody final FlottenfahrzeugDTO flottenfahrzeugDTO,
        final HttpServletRequest request
    ) throws URISyntaxException {
        log.debug("create: {}", flottenfahrzeugDTO);

        final var flottenfahrzeug = service.create(flottenfahrzeugDTO.toFlottenfahrzeug());
        final var baseUri = getBaseUri(request);
        final var location = new URI(baseUri + "/" + flottenfahrzeug.getId()); //NOSONAR
        return created(location).build();


    }

    /**
     * Einen vorhandenen flottenfahrzeug-Datensatz überschreiben.
     *
     * @param id ID des zu aktualisierenden Flottenfahrzeug.
     * @param flottenfahrzeugDTO Das Flottenfahrzeugnobjekt aus dem eingegangenen Request-Body.
     * @return Response mit Statuscode 204 oder Statuscode 422, falls Constraints verletzt sind oder
     *      der JSON-Datensatz syntaktisch nicht korrekt ist oder falls die Emailadresse bereits existiert oder
     *      Statuscode 400 falls syntaktische Fehler im Request-Body vorliegen.
     */
    @PutMapping(path = "{id:" + ID_PATTERN + "}", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Einen Flottenfahrzeugn mit neuen Werten aktualisieren", tags = "Aktualisieren")
    @ApiResponse(responseCode = "204", description = "Aktualisiert")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "404", description = "Flottenfahrzeug nicht vorhanden")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte oder Email vorhanden")
    ResponseEntity<Void> update(
        @PathVariable final UUID id,
        @RequestBody final FlottenfahrzeugDTO flottenfahrzeugDTO
    ) {
        log.debug("update: id={}, {}", id, flottenfahrzeugDTO);
        service.update(flottenfahrzeugDTO.toFlottenfahrzeug(), id);
        return noContent().build();
    }



}
