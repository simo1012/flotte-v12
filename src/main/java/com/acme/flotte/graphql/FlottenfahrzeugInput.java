package com.acme.flotte.graphql;

import com.acme.flotte.entity.Flottenfahrzeug;
import com.acme.flotte.entity.GetriebeType;
import com.acme.flotte.entity.MotorType;
import com.acme.flotte.entity.Zulassungsadresse;

import java.net.URL;
import java.time.LocalDate;

/**
 * Eine Value-Klasse für Eingabedaten passend zu FlottenfahrzeugInput aus dem GraphQL-Schema.
 *
 * @param kennzeichen Kennzeichen
 * @param fahrzeugmodell Fahrzeugmodell
 * @param zulassungsadresse Zulassungsadresse
 * @param motorType MotorType
 * @param getriebeType GetriebeType
 * @param infleet Infleet
 * @param homepage Homepage
 * @param email Email
 */
record FlottenfahrzeugInput(
    String kennzeichen,
    String fahrzeugmodell,
    ZulassungsadresseInput zulassungsadresse,
    MotorType motorType,
    GetriebeType getriebeType,
    String infleet,
    URL homepage,
    String email
) {
    /**
     * Konvertierung in ein Objekt der Entity-Klasse Flotte.
     *
     * @return Das konvertierte Flotten-Objekt
     */

    Flottenfahrzeug toFlottenfahrzeug() {
        final LocalDate infleetTmp;
        infleetTmp = LocalDate.parse(infleet);
        final var adresseTmp = Zulassungsadresse.builder()
            .plz(zulassungsadresse.plz()).ort(zulassungsadresse.ort()).build();

        return Flottenfahrzeug
            .builder()
            .id(null)
            .kennzeichen(kennzeichen)
            .fahrzeugmodell(fahrzeugmodell)
            .zulassungsadresse(adresseTmp)
            .motorType(motorType)
            .infleet(infleetTmp)
            .getriebeType(getriebeType)
            .homepage(homepage)
            .email(email)
            .build();

    }
}
