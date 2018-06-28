package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.farbe

// Created by julian on 28.06.18.

/**
 * Verwendet die Feinabstimmung.
 * Sonst wie [HsvFarbkreisLogarithmisch]
 */
class HsvFkLogGeglättet: HsvFarbkreisLogarithmisch() {
    override fun calculateFraction(iterationen: Int, feinjustierung: Double): Double {
        // Zwischen 0 und 1 begrenzen
        // Die Werte der Feinabsimmung sind ebenfalls logarithmisch verteilt.
        val feinj: Double = Math.min(1.0, Math.max(0.0, Math.log10(1.0+ 9*feinjustierung) ))

        /* Feinjustierung:
         * Zwischen dieser und der höheren Iteration
         * Problem: Auf einem Kreis haben 0.1 und 0.9 nur 0.2 "Abstand",
         * und nicht 0.8 wie normal. */
        val fra0 = iterationToFraction(iterationen)
        val fra1 = iterationToFraction(iterationen +1)
        val abstand: Double
        if(fra1 > fra0) abstand = fra1 -fra0
        else abstand = fra1+1.0 -fra0

        // Fraction plus die gewichtete Differenz
        var fraction = fra0 + feinj * abstand
        if(fraction > 1.0) fraction -= 1.0
        return fraction
    }

    override fun toString(): String {
        return "Hsv Log Geglättet"
    }
}
