package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.farbe

import javafx.scene.paint.Color
import java.lang.Math.max
import java.lang.Math.min

// Created by julian on 22.12.17.
/**
 * Siehe [berechneFarbe].
 */
class HsvFarbkreisLogarithmisch: FarbAlgorithmus() {

    /**Die Farbe wird berechnet mit dem HSV-Farbkreis.<br></br>
     * Der HSV-Farbkreis wird sozusagen
     * einmal rundumgegangen mit den ersten 15 Iterationen. Für die
     * zweite Runde werden 31 Iterationen benötigt.<br></br>
     * Die benötigten Iterationen fürs rundumgehen wachsen
     * exponentiell, da die Iterationen exponentiell gegen unendlich gehen,
     * wenn man sich der Grenzlinie nähert.<br></br>
     * Die HSV-Werte werden noch in RGB umgewandelt.

     * @param iterationen Die Anzahl der Iterationen, bis der Punkt abgehaut ist.
     * @param maxIterationen Wenn die Iteration größergleich maxIterationen
     * ist, wird die Grundfarbe verwendet.
     * @param grundfarbe Die Farbe, die der Mittelteil des Apfelmännchens bekommt.
     * @return Die Farbe als Int im argb-Format
     */
    override fun berechneFarbe(iterationen: Int, maxIterationen: Int, grundfarbe: Color, feinjustierung: Double): Int {

        /** Die Farbe als Int im argb-Format */
        val color: Int
        val fraction: Double

        if (iterationen == maxIterationen)
            color = colorToArgbInt(grundfarbe)
        else {
            // Zwischen 0 und 1 begrenzen
            val feinj = min(1.0, max(0.0, feinjustierung))

            // Feinjustierung:
            // Zwischen dieser und der höheren Iteration
            fraction = (1.0-feinj) * iterationToFraction(iterationen)
                           + feinj * iterationToFraction(iterationen -1)

            //println("${iterationToFraction(iterationen)} \t${iterationToFraction(iterationen -1)} \t$feinj")
            //fraction = iterationToFraction(iterationen)

            color = HsvHueToArgb(fraction)
        }
        return color
    }

    fun iterationToFraction(iterationen: Int): Double {
        @Suppress("NAME_SHADOWING")
        var iterationen = iterationen
        /* Exponentielles Verhalten. Der HSV-Farbkreis wird sozusagen
         * einmal rundumgegangen mit den ersten 15 Iterationen. Für die
         * zweite Runde werden 31 Iterationen benötigt, für die Dritte
         * noch mal doppelt so viele. */
        iterationen += 8
        var runde = 15
        while (iterationen >= runde)
        // Bitmuster ist immer nur einsen, somit kein Problem mit MAX_INT >= runde.
            runde = runde * 2 + 1

        /* iterationen ist nun zwischen runde/2 und runde.
         * Deshalb zuerst minus runde/2; dadurch ist es zwischen 0 und runde/2.
         * Und nur noch in den Bereich von 0 bis 1 bingen. */
        val fraction = (iterationen - runde / 2) / (runde / 2).toDouble()
        return fraction
    }

    override fun toString(): String {
        return "HSV Farbkreis Logarithmisch"
    }
}