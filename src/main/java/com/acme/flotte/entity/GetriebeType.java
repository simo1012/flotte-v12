package com.acme.flotte.entity;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum für Getriebe. Dazu können auf der Clientseite z.B. Radiobuttons realisiert werden.
 */
public enum GetriebeType {
    /**
     * _Manuell_ mit dem internen Wert 'Man' für z.B. das Mapping in einem JSON-Datensatz oder
     *      * das Abspeichern in einer DB.
     */
    MANUELL("Man"),
    /**
     * _Automatik_ mit dem internen Wert 'Aut' für z.B. das Mapping in einem JSON-Datensatz oder
     *      * das Abspeichern in einer DB.
     */
    AUTOMATIK("Aut");
    private final String value;

    GetriebeType(final String value) {
        this.value = value;
    }

    /**
     * Einen enum-Wert als String mit dem internen Wert ausgeben.
     * Dieser Wert wird durch Jackson in einem JSON-Datensatz verwendet.
     * [<a href="https://github.com/FasterXML/jackson-databind/wiki">Wiki-Seiten</a>]
     *
     * @return Interner Wert
     */
    @JsonValue
    @Override
    public String toString() {
        return value;
    }
}
