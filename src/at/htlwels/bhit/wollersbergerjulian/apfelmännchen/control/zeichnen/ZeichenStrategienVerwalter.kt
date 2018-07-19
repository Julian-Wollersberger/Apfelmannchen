package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control.zeichnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control.ZeichenflächeController
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import javafx.scene.layout.Pane
import javafx.stage.Window


// Created by julian on 19.12.17.
/**
 * Schnittstelle zwischen den Strategien (wie
 * genau das Bild verarbeitet/angezeigt wird)
 * und der Außenwelt.
 *
 * Strategie muss sofort festgesetzt werden!
 */
class ZeichenStrategienVerwalter(
        private val controller: ZeichenflächeController,
        private val elternPane: Pane
) {
    // Aufpassen, dass es nicht null ist, wenn es gebraucht wird!
    private lateinit var strategie: ZeichenStrategie

    /** Die Zeichner sollen es nicht verändern können. */
    val koordsys: DoppelKoordinatenSystem
        get() = controller.globalesKoordsys

    val parameter: ApfelmännchenParameter
        get() = controller.eingaben.parameter

    val anzahlThreads: Int
        get() = controller.eingaben.anzahlThreads

    /** Berechnet die Bilder aller Strategien. */
    fun aktualisiere() {
        strategie.aktualisiere()
    }

    /****** Factory Methods für die Strategien. ******/
    fun setSimpleStrategie() {
        strategie = SimpleStrategie(elternPane, this)
    }
    fun setMultithreadedStrategie() {
        strategie = MultithreadedStrategie(elternPane, this)
    }
    fun setGpuStrategie() {
        strategie = GpuStrategie(elternPane, this)
    }

    /** Diese Strategie ist ziemlich anders und passt icht wirklich ins
     * Schema. Sie sucht sich ihre Werte direkt vom Controller,
     * was diese Klasse überflüssig macht.
     * @see BildSpeichernStrategie */
    fun setBildSpeichernStrategie(controller: ZeichenflächeController) {
        strategie = BildSpeichernStrategie(elternPane, this, controller)
    }
}