package com.acme.flotte.rest;

import com.acme.flotte.entity.Flottenfahrzeug;
import com.acme.flotte.entity.GetriebeType;
import com.acme.flotte.entity.MotorType;
import com.acme.flotte.entity.Zulassungsadresse;

import java.net.URL;
import java.time.LocalDate;

/**
 * ValueObject für das Neuanlegen und Ändern eines neuen Flottenfahrzeugs.
 * Beim Lesen wird die Klasse FlottenfahrzeugModel für die Ausgabe
 * verwendet.
 *
 * @param kennzeichen Das Kennzeichen des Flottenfahrzeugs.
 * @param fahrzeugmodell  Das fahrzeugmodell des Flottenfahrzeugs.
 * @param zulassungsadresse  Die zulassungsadresse des Flottenfahrzeugs.
 * @param motorType  Der Motor des Flottenfahrzeugs.
 * @param infleet  Das Zulassungsdatum des Flottenfahrzeugs.
 * @param getriebeType  Das Getriebe des Flottenfahrzeugs.
 * @param homepage  Doe Homepage des Flottenfahrzeugs.
 * @param email  Die Email des Flottenfahrzeugs.
 */
@SuppressWarnings("RecordComponentNumber")
record FlottenfahrzeugDTO(
    String kennzeichen,
    String fahrzeugmodell,
    Zulassungsadresse zulassungsadresse,
    MotorType motorType,
    LocalDate infleet,
    GetriebeType getriebeType,
    URL homepage,
    String email
) {
    /**
     * Konvertierung in ein Objekt des Anwendungskerns.
     *
     * @return Flottenfahrzeugobjekt für den Anwendungskern
     */
    Flottenfahrzeug toFlottenfahrzeug() {
        return Flottenfahrzeug
                .builder()
                .id(null)
                .kennzeichen(kennzeichen)
                .fahrzeugmodell(fahrzeugmodell)
                .zulassungsadresse(zulassungsadresse)
                .motorType(motorType)
                .infleet(infleet)
                .getriebeType(getriebeType)
                .homepage(homepage)
                .email(email)
                .build();

    }

}
