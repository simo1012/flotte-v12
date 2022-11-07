package com.acme.flotte.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Zulassungadresse der Fahrzeuge für die Anwendunglogik zum Abspeichern in der DB.
 */
@Builder
@Getter
@Setter
@ToString
@SuppressWarnings({"JavadocDeclaration", "RequireEmptyLineBeforeBlockTagGroup"})
public class Zulassungsadresse {
    /**
     * Konstante für den regulären Ausdruck einer Postleitzahl als 5-stellige Zahl mit führender Null.
     */
    public static final String PLZ_PATTERN = "^\\d{5}$";
    /**
     * Die Postleitzahl für die Zulassungsadresse.
     */
    @NotEmpty
    @Pattern(regexp = PLZ_PATTERN)
    private String plz;
    /**
     * Der Ort der Zulassungsadresse.
     */
    @NotEmpty
    private String ort;
}
