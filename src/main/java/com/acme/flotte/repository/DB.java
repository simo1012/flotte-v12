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
            .kennzeichen("MVV1337")
            .id(UUID.fromString("00000000-0000-0000-0000-000000000000"))
            .fahrzeugmodell("BMW 318i")
            .zulassungsadresse(Zulassungsadresse.builder().plz("68163").ort("Mannheim").build())
            .motorType(MotorType.Benzin)
            .infleet(LocalDate.parse("2022-01-13"))
            .getriebeType(GetriebeType.MANUELL)
            .hompage(new URL("www.bmw.de"))
            .build(),

        Flottenfahrzeug.builder()
            .kennzeichen("MDV1526")
            .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
            .fahrzeugmodell("Mercedes C63")
            .zulassungsadresse(Zulassungsadresse.builder().plz("76131").ort("Karlsruhe").build())
            .motorType(MotorType.Benzin)
            .infleet(LocalDate.parse("2022-09-13"))
            .getriebeType(GetriebeType.AUTOMATIK)
            .hompage(new URL("www.Mercedes-benz.de"))
            .build(),

            Flottenfahrzeug.builder()
                .kennzeichen("MDA1526")
                .id(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                .fahrzeugmodell("Mercedes S500e")
                .zulassungsadresse(Zulassungsadresse.builder().plz("10232").ort("Berlin").build())
                .motorType(MotorType.BENZINHYBRID)
                .infleet(LocalDate.parse("2022-09-13"))
                .getriebeType(GetriebeType.AUTOMATIK)
                .hompage(new URL("www.Mercedes-benz.de"))
                .build(),

            Flottenfahrzeug.builder()
                .kennzeichen("MKR1526")
                .id(UUID.fromString("00000000-0000-0000-0000-000000000003"))
                .fahrzeugmodell("Audi A6 TFSI 55e")
                .zulassungsadresse(Zulassungsadresse.builder().plz("20232").ort("Hamburg").build())
                .motorType(MotorType.BENZINHYBRID)
                .infleet(LocalDate.parse("2022-10-13"))
                .getriebeType(GetriebeType.AUTOMATIK)
                .hompage(new URL("www.Audi.de"))
                .build()

        )
        .collect(Collectors.toList());

    }


}
