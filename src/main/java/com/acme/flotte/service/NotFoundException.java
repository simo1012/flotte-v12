package com.acme.flotte.service;

import lombok.Getter;
import java.util.Map;
import java.util.UUID;

/**
 * RuntimeException, falls kein Flottenfahrzeug gefunden wurde.
 */

@Getter
@SuppressWarnings("ParameterHidesMemberVariable")
public final class NotFoundException extends RuntimeException {

    /**
     * Nicht vorhandene ID.
     */
    private final UUID id;

    /**
     * Suchkriterien, zu denen nichts gefunden wurde.
     */
    private final Map<String, String> suchkriterien;

    NotFoundException(final UUID id) {
        super("Kein Flottenfahrzeug mit der ID " + id + " gefunden.");
        this.id = id;
        //noinspection AssignmentToNull
        suchkriterien = null;
    }

    NotFoundException(final Map<String, String> suchkriterien) {
        super("Keine Kundee gefunden.");
        //noinspection AssignmentToNull
        id = null;
        this.suchkriterien = suchkriterien;
    }

    @SuppressWarnings("AssignmentToNull")
    NotFoundException() {
        super("Keine Kunden gefunden.");
        id = null;
        suchkriterien = null;
    }
}
