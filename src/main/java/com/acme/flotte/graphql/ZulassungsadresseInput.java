package com.acme.flotte.graphql;

/**
 * Adressdaten.
 *
 *
 * @param plz Die 5-stellige Postleitzahl als unveränderliches Pflichtfeld.
 * @param ort Der Ort als unveränderliches Pflichtfeld.
 */
record ZulassungsadresseInput(String plz, String ort) {
}
