package com.acme.flotte.rest;

import com.acme.flotte.entity.Flottenfahrzeug;
import com.acme.flotte.entity.GetriebeType;
import com.acme.flotte.entity.MotorType;
import com.acme.flotte.entity.Zulassungsadresse;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.net.URL;
import java.time.LocalDate;

/**
 * Model-Klasse für Spring HATEOAS. @lombok.Data fasst die Annotationsn @ToString, @EqualsAndHashCode, @Getter, @Setter
 * und @RequiredArgsConstructor zusammen.
 *
 */


@JsonPropertyOrder({"kennzeichen", "fahrzeugmodell", "zulassungsadresse",
    "motorType", "infleet", "getriebeType", "homepage", "email"})
@Relation(collectionRelation = "flottenfahrzeuge", itemRelation = "flottenfahrzeug")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@ToString(callSuper = true)
class FlottenfahrzeugModel extends RepresentationModel<FlottenfahrzeugModel> {
    private final String kennzeichen;

    private final String fahrzeugmodell;

    private final Zulassungsadresse zulassungsadresse;

    private final MotorType motorType;

    private final LocalDate infleet;

    private final GetriebeType getriebeType;

    private final URL homepage;

    @EqualsAndHashCode.Include
    private final String email;

    FlottenfahrzeugModel(final Flottenfahrzeug flottenfahrzeug) {
        kennzeichen = flottenfahrzeug.getKennzeichen();
        fahrzeugmodell = flottenfahrzeug.getFahrzeugmodell();
        zulassungsadresse = flottenfahrzeug.getZulassungsadresse();
        motorType = flottenfahrzeug.getMotorType();
        infleet = flottenfahrzeug.getInfleet();
        getriebeType = flottenfahrzeug.getGetriebeType();
        homepage = flottenfahrzeug.getHomepage();
        email = flottenfahrzeug.getEmail();

    }

}
