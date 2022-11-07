package com.acme.flotte.entity;

import com.fasterxml.jackson.annotation.JsonValue;
/**
 * Enum für Motorisierung. Dazu kann auf der Clientseite z.B. ein Dropdown-Menü realisiert werden.
 */
public enum MotorType {
    /**
     * _Benzin_ mit internen Wert `BE` für z.B. das Mapping in einem JSON-Datensatz oder das Abspeichern in einer DB.
     */
    Benzin("BE"),
    /**
     * _Diesel_ mit internen Wert `DI` für z.B. das Mapping in einem JSON-Datensatz oder das Abspeichern in einer DB.
     */
    DIESEL("DI"),
    /**
     * _Benzin/Hybrid_ mit internen Wert `BEHY` für z.B. das Mapping in
     * einem JSON-Datensatz oder das Abspeichern in einer DB.
     */
    BENZINHYBRID("BEHY"),
    /**
     * _Diesel/Hybrid_ mit internen Wert `DIHY` für z.B. das Mapping
     * in einem JSON-Datensatz oder das Abspeichern in einer DB.
     */
    DIESELHYBDRID("DIHY");
    private final String value;
    MotorType(final String value) {
        this.value = value; }
    /**
     * Einen enum-Wert als String mit dem internen Wert ausgeben.
     * Dieser Wert wird durch Jackson in einem JSON-Datensatz verwendet.
     * [<a href="https://github.com/FasterXML/jackson-databind/wiki">Wiki-Seiten</a>]
     *
     * @return Der interne Wert.
     */

    @JsonValue
    @Override
    public String toString() {
        return value;
    }
}
