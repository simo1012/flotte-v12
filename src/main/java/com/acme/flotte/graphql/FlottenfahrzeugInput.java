package com.acme.flotte.graphql;

import com.acme.flotte.entity.Flottenfahrzeug;
import com.acme.flotte.entity.GetriebeType;
import com.acme.flotte.entity.MotorType;
import com.acme.flotte.entity.Zulassungsadresse;

import java.net.URL;
import java.time.LocalDate;

record FlottenfahrzeugInput(
    String kennzeichen,
    String fahrzeugmodell,
    ZulassungsadresseInput zulassungsadresse,
    MotorType motorType,
    GetriebeType getriebeType,
    String infleet,
    URL hompage,
    String email
) {
    /**
     * Konvertierung in ein Objekt der Entity-Klasse Flotte.
     *
     * @return Das konvertierte Flotten-Objekt
     */

    Flottenfahrzeug toFlottenfahrzeug(){
        final LocalDate infleetTmp;
        infleetTmp = LocalDate.parse(infleet);
        final var adresseTmp = Zulassungsadresse.builder().plz(zulassungsadresse.plz()).ort(zulassungsadresse.ort()).build();

        return Flottenfahrzeug
            .builder()
            .id(null)
            .kennzeichen(kennzeichen)
            .fahrzeugmodell(fahrzeugmodell)
            .zulassungsadresse(adresseTmp)
            .motorType(motorType)
            .infleet(infleetTmp)
            .getriebeType(getriebeType)
            .homepage(hompage)
            .email(email)
            .build();

    }
}
