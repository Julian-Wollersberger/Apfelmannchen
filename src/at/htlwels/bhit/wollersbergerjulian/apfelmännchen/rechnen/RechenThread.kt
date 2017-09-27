package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.BerechnungsParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import javafx.scene.image.WritableImage

// Created by julian on 29.07.17.
/**
 * Diese Klasse ist ein Thread, der die Berechnung
 * des Bildes übernimmt.
 * Nachdem die Berechnung abgeschlossen ist, wird
 * die übergebene Funktion [fertig] ausgeführt.
 *
 * @param neuesImage auf dieses Bild wird geschrieben. In fertig() wird es dann
 */
class RechenThread(val berechnungsParameter: BerechnungsParameter,
                   val fertig: (WritableImage, DoppelKoordinatenSystem) -> Unit ,
                   val neuesImage: WritableImage, val imageKoordsys: DoppelKoordinatenSystem)
    : Thread("Berechner") {
    init {
        isDaemon = true
    }

    override fun run() {
        berechneBereich(berechnungsParameter)
        fertig(neuesImage, imageKoordsys)
    }
}