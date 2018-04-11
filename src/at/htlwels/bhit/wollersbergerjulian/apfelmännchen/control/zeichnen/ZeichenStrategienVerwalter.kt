package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control.zeichnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control.ZeichenflächeController
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import javafx.scene.layout.Pane


// Created by julian on 19.12.17.
/**
 * Schnittstelle zwischen den Strategien (wie
 * genau das Bild verarbeitet/angezeigt wird)
 * und der Außenwelt.
 *
 * Es kann mehrere Strategien gleichzeitig geben. Dann
 * werden alle angesprochen.
 */
class ZeichenStrategienVerwalter(
        private val controller: ZeichenflächeController,
        private val elternPane: Pane
) {
    /** Das Löschen wird noch nicht gemacht. */
    private val strategienListe = ArrayList<ZeichenStrategie>()

    /** Die Zeichner sollen es nicht verändern können. */
    val koordsys: DoppelKoordinatenSystem
        get() = controller.globalesKoordsys

    val parameter: ApfelmännchenParameter
        get() = controller.eingaben.parameter

    val anzahlThreads: Int
        get() = controller.eingaben.anzahlThreads

    /** Berechnet die Bilder aller Strategien. */
    fun aktualisiere() {
        strategienListe.forEach {
            it.aktualisiere()
        }
    }

    /****** Factory Methods für die Strategien. ******/
    fun addSimpleStrategie() {
        strategienListe.add(
                SimpleStrategie(elternPane, this)
        )
    }
    fun addMultithreadedStrategie() {
        strategienListe.add(
                MultithreadedStrategie(elternPane, this)
        )
    }
}