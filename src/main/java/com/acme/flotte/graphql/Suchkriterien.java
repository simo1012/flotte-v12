package com.acme.flotte.graphql;

import java.util.HashMap;
import java.util.Map;

/**
 * Eine Value-Klasse für Eingabedaten passend zu Suchkriterien aus dem GraphQL-Schema.
 *
 * @author <a href="mailto:simo1012@h-ka.de">Mohamed Simpara</a>
 *
 * @param kennzeichen Nachname
 * @param email Emailadresse
 */
record Suchkriterien(
    String kennzeichen,
    String email
) {
    /**
     * Konvertierung in eine Map.
     *
     * @return Das konvertierte Map-Objekt
     */
    Map<String, String> toMap() {
        @SuppressWarnings("TypeMayBeWeakened")
        final var map = new HashMap<String, String>(2, 1);
        if (kennzeichen != null) {
            map.put("kennzeichen", kennzeichen);
        }
        if (email != null) {
            map.put("email", email);
        }
        return map;
    }

}
