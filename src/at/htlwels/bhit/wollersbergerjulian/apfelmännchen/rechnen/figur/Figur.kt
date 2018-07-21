package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.figur

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem


// Created by julian on 21.07.18.
/**
 * Das Apfelmännchen ist eine Figur.
 * Es wird charakterisiert durch seinen [istInMenge] Algorithmus
 */
abstract class Figur {

    /** @see ApfelmännchenFigur.istInMenge */
    abstract fun istInMenge(cr: Double, ci: Double, maxIter: Int, maxDistanz: Double): Double

    /** Der Text, der im GUI in der ChoiceBox angezeigt werden soll. */
    override abstract fun toString(): String

    companion object {
        /**Berechnet jeden Punkt im Bereich des koordsys die übergebene Funktion.
         *
         * @param koordsys Dessen Bereich  wird gezeichnet
         * und dessen Breite und Höhe sind die Dimensionen des Bildes.
         * @param doSomething Diese Funktion wird aufgerufen. Die
         * Parameter sind die Pixel-Koordinaten und die komplexen Koordinaten
         * für diesen Punkt.
         */
        fun fürJedenPunkt(
                koordsys: DoppelKoordinatenSystem,
                doSomething: (x: Int, y: Int, cr: Double, ci: Double) -> Unit
        ) {
            var cr = koordsys.kxMin
            var ci = koordsys.kyMax

            for (i in 0..(koordsys.spaltenzahl-1)) {
                // Kein += damit weniger Rundungsfehler
                cr = koordsys.kxMin + koordsys.schrittR * i
                for (j in 0..(koordsys.zeilenzahl-1)) {
                    ci = koordsys.kyMax + koordsys.schrittI * j

                    doSomething(i, j, cr, ci)
                }
            }
        }
    }
}