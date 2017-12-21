package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control.zeichnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.BildAnimierer
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.Verzögerer
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.berechneAnimiert
import javafx.application.Platform
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane


// Created by julian on 20.12.17.
/**
 * TODO Description
 */
class AnimiertMultithreadedStrategie(
        elternPane: Pane,
        verwalter: ZeichenStrategienVerwalter
) : ZeichenStrategie(elternPane, verwalter) {

    override fun aktualisiere() {

    }
}