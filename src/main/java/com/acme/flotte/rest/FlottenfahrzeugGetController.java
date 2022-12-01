package com.acme.flotte.rest;

import com.acme.flotte.entity.Flottenfahrzeug;
import com.acme.flotte.service.FlottenfahrzeugReadService;
import com.acme.flotte.service.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
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
    private final UriHelper uriHelper;

    /**
     * Suche anhand der Flottenfahrzeug-ID als Pfad-Parameter.
     *
     * @param id ID des zu suchenden Flottenfahrzeugs
     * @param request Das Request-Objekt, um Links für HATEOAS zu erstellen.
     * @return Ein Response mit dem Statuscode 200 und dem gefundenen Flottenfahrzeug mit oder Statuscode 404.
     */
    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Suche mit der Flottenfahrzeug-ID", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Flottenfahrzeug gefunden")
    @ApiResponse(responseCode = "404", description = "Flottenfahrzeug nicht gefunden")
    Flottenfahrzeug findById(@PathVariable final UUID id, final HttpServletRequest request) {
        log.debug("findById: id={}", id);

        final var flottenfahrzeug = service.findById(id);

        // HATEOAS
        final var model = new FlottenfahrzeugModel(flottenfahrzeug);
        // evtl. Forwarding von einem API-Gateway
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var idUri = baseUri + '/' + flottenfahrzeug.getId();
        final var selfLink = Link.of(idUri);
        final var listLink = Link.of(baseUri, LinkRelation.of("list"));
        final var addLink = Link.of(baseUri, LinkRelation.of("add"));
        final var updateLink = Link.of(idUri, LinkRelation.of("update"));
        final var removeLink = Link.of(idUri, LinkRelation.of("remove"));
        model.add(selfLink, listLink, addLink, updateLink, removeLink);

        log.debug("findById: {}", flottenfahrzeug);

        return flottenfahrzeug;



    }



    /**
     * Suche mit diversen Suchkriterien als Query-Parameter.
     *
     * @param suchkriterien Query-Parameter als Map.
     * @param request Das Request-Objekt, um Links für HATEOAS zu erstellen.
     * @return Gefundenes Flottenfahrzeug als CollectionModel.
     */

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Suche mit Suchkriterien", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "CollectionModel mid den Flottenfahrzeuge")
    @ApiResponse(responseCode = "404", description = "Keine Flottenfahrzeuge gefunden")
    CollectionModel<FlottenfahrzeugModel> find(
        @RequestParam final Map<String, String> suchkriterien,
        final HttpServletRequest request

    ) {
        log.debug("find: suchkriterien={}", suchkriterien);

        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var models = service.find(suchkriterien)
                .stream()
                .map(flottenfahrzeug -> {
                    final var model = new FlottenfahrzeugModel(flottenfahrzeug);
                    model.add(Link.of(baseUri + '/' + flottenfahrzeug.getId()));
                    return model;
                })
                .toList();

        log.debug("find: {}", models);
        return CollectionModel.of(models);
    }

    @ExceptionHandler
    @ResponseStatus
    void onNotFound(final NotFoundException ex) {
        log.debug("onNotFound: {}", ex.getMessage());

    }


}
