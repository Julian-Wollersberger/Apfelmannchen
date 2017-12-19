package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control.zeichnen


// Created by julian on 06.12.17.
/**
 * Wie soll das Bild in den SceneGraph eingefügt werden?
 * Wenn es fertig ist, animiert oder doch nur speichern?
 *
 * Nach dem Strategy Pattern. TODO Link
 * Mit dem Unterschied, dass mehrere Strategien gleichzeitig sein können.
 * (z.B. speichern und anzeigen und Punkte anzeigen und vielleicht Screenshot?)
 */
abstract class AnzeigeStrategie {
    abstract fun berechne()
}