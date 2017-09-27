package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.berechneBereich
import javafx.scene.paint.Color

// Created by julian on 29.07.17.
/**
 * Alle Parameter, die eine Berechnung der Mandelbrotmenge so braucht.
 * Dies hier sind exakt die Parameter, die [berechneBereich] braucht.
 *
 * TODO DoppelKoordsys statt Bereich, Spaltenzahl und Zeilenzahl
 */
data class BerechnungsParameter(
        val bereich: Bereich, val spaltenzahl: Int, val zeilenzahl: Int,
        val maxIterationen: Int, val distanz: Double, val grundfarbe: Color,
        val zeichnePunkt: (x: Int, y: Int, argb: Int) -> Unit)
