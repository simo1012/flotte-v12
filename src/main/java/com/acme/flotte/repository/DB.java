package com.acme.flotte.repository;

import com.acme.flotte.entity.Flottenfahrzeug;
import com.acme.flotte.entity.GetriebeType;
import com.acme.flotte.entity.MotorType;
import com.acme.flotte.entity.Zulassungsadresse;
import lombok.SneakyThrows;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Emulation der Datenbasis für persistente Flottenfahrzeuge.
 */

@SuppressWarnings({"UtilityClassCanBeEnum", "UtilityClass", "MagicNumber", "RedundantSuppression"})
final class DB {
    /**
     * Liste der Flottenfahrzeuge zur Emulation der DB.
     */
    @SuppressWarnings("StaticCollection")
    static final List<Flottenfahrzeug> FLOTTENFAHRZEUGE = getFlottenfahrzeuge();
    private DB() {
    }
    @SneakyThrows(MalformedURLException.class)
    @SuppressWarnings({"FeatureEnvy", "TrailingComment"})
    private static List<Flottenfahrzeug> getFlottenfahrzeuge() {
        return Stream.of(
        Flottenfahrzeug.builder()
            .id(UUID.fromString("00000000-0000-0000-0000-000000000000"))
            .kennzeichen("MVV1337")
            .fahrzeugmodell("BMW318i")
            .zulassungsadresse(Zulassungsadresse.builder().plz("68163").ort("Mannheim").build())
            .motorType(MotorType.Benzin)
            .infleet(LocalDate.parse("2022-01-13"))
            .getriebeType(GetriebeType.MANUELL)
            .homepage(new URL("https://www.Bmw.de"))
            .email("mvv1337@acme.com")
            .build(),

        Flottenfahrzeug.builder()
            .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
            .kennzeichen("MDV1526")
            .fahrzeugmodell("Mercedes C63")
            .zulassungsadresse(Zulassungsadresse.builder().plz("76131").ort("Karlsruhe").build())
            .motorType(MotorType.Benzin)
            .infleet(LocalDate.parse("2022-09-13"))
            .getriebeType(GetriebeType.AUTOMATIK)
            .homepage(new URL("https://www.Mercedes-benz.de"))
            .email("mdv1526@acme.com")
            .build(),

            Flottenfahrzeug.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                .kennzeichen("MDA1526")
                .fahrzeugmodell("Mercedes S500e")
                .zulassungsadresse(Zulassungsadresse.builder().plz("10232").ort("Berlin").build())
                .motorType(MotorType.BENZINHYBRID)
                .infleet(LocalDate.parse("2022-09-13"))
                .getriebeType(GetriebeType.AUTOMATIK)
                .homepage(new URL("https://www.Mercedes-benz.de"))
                .email("mda1526@acme.com")
                .build(),

            Flottenfahrzeug.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000003"))
                .kennzeichen("MKR1526")
                .fahrzeugmodell("Audi A6 TFSI 55e")
                .zulassungsadresse(Zulassungsadresse.builder().plz("20232").ort("Hamburg").build())
                .motorType(MotorType.BENZINHYBRID)
                .infleet(LocalDate.parse("2022-10-13"))
                .getriebeType(GetriebeType.AUTOMATIK)
                .homepage(new URL("https://www.Audi.de"))
                .email("mkr1526@acme.com")
                .build()

        )
        .collect(Collectors.toList());

    }


}
