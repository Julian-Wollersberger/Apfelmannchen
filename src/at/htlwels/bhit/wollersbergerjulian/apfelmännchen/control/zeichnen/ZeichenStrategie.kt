package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control.zeichnen

import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane


// Created by julian on 06.12.17.
/**
 * Wie soll das Bild in den SceneGraph eingefügt werden?
 * Wenn es fertig ist, animiert oder doch nur speichern?
 *
 * Nach dem Strategy Pattern. TODO Link
 * Mit dem Unterschied, dass mehrere Strategien gleichzeitig sein können.
 * (z.B. speichern und anzeigen und Punkte anzeigen und vielleicht Screenshot?)
 */
abstract class ZeichenStrategie(
        elternPane: Pane,
        val verwalter: ZeichenStrategienVerwalter
) {

    /** gehört nur dieser Strategie. */
    protected val zeichenPane: Pane = VerkleinerbaresPane()

    /** Initialisiert das zeichenPane und
     * fügt es zum elternPane hinzu. */
    init {
        /* Die ZeichenRegion soll sich mitvergrößern. */
        AnchorPane.setLeftAnchor(zeichenPane, 0.0)
        AnchorPane.setRightAnchor(zeichenPane, 0.0)
        AnchorPane.setTopAnchor(zeichenPane, 0.0)
        AnchorPane.setBottomAnchor(zeichenPane, 0.0)

        elternPane.children.add(zeichenPane)
        // Wird nie entfernt!
    }

    abstract fun aktualisiere()


    /**Damit das Eltern-AnchorPane das zeichenPane verkleinern kann,
     * muss seine bevorzugte Größe minWidth sein.
     * (Sonst wird es nie kleiner als das bereits berechnete Bild) */
    class VerkleinerbaresPane : Pane() {

        override fun computePrefWidth(height: Double): Double {
            // entspricht Insets
            return super.computeMinWidth(height)
        }
        override fun computePrefHeight(width: Double): Double {
            // entspricht Insets
            return super.computeMinHeight(width)
        }
    }
}