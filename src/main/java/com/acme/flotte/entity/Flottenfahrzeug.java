package com.acme.flotte.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.URL;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Unveränderliche Daten eines Flottenfahrzeug. In DDD ist Flottenfahrzeug ist ein Aggregate Root.
 */
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@ToString
@SuppressWarnings({"ClassFanOutComplexity", "JavadocDeclaration", "RequireEmptyLineBeforeBlockTagGroup"})
public class Flottenfahrzeug {

    /**
     * Muster für ein gültiges Kennzeichen.
     */

    public static final String KENNZEICHEN_PATTERN = "M[A-Z][A-Z][1-9][0-9][0-9][0-9]";


    /**
     * Die interne ID des Flottenfahrzeugs.
     */
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Das Kennzeichen den Flottenfahrzeugs.
     */
    @NotEmpty
    @Pattern(regexp = KENNZEICHEN_PATTERN)
    private String kennzeichen;

    /**
     * Das Fahrzeugmodell aus der Flotte.
     */

    @NotEmpty
    private String fahrzeugmodell;
    /**
     * Die Zulassungadresse des Flottenfahrzeugs.
     */
    @Valid
    @ToString.Exclude
    private Zulassungsadresse zulassungsadresse;
    /**
     * Die Motorisierung des Flottenfahrzeugs.
     */
    private MotorType motorType;
    /**
     * Das Zulassungsdatum des Flottenfahrzeugs.
     */
    @Past
    private LocalDate infleet;
    /**
     * Das Getriebe des Flottenfahrzeugs.
     */
    private GetriebeType getriebeType;

    /**
     * Die URL zur Homepage des Flottenfahrzeugs.
     */
    private URL homepage;

    /**
     * Die Emailadresse des Flottenfahrzeug.
     * @param email Die Emailadresse.
     * @return Die Emailadresse.
     */
    @Email
    @NotNull
    private String email;
}
