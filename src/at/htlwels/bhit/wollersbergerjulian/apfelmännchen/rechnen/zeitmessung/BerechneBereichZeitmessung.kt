package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.zeitmessung

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.BerechnungMultithreaded
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.StandardwerteEingabe
import org.junit.jupiter.api.Test


// Created by julian on 06.12.17.
/**
 * Ergebnis:
 * Es braucht 4s273ms bis 4s327ms
 *
 * Damit braucht es genausolange, wie die
 * istInMenge. Die Berechnung der Farbe fällt damit
 * nicht ins Gewicht.
 */
class BerechneBereichZeitmessung {

    /** Damit es leichter ist, habe ich ein paar
     * Werte im Debugger kopiert, in einem Bereich, in
     * dem allses weis war.
     *
     * cr = -0.4860438957475998
     * ciMax = 0.2749190672153638
     * aktuelleSpalte = 0
     * zeilenzahl = 449
     * schrittI = -7.038961753141418E-4
     */
    @Test
    fun berechneSpalteZeitmessung() {
        val multi = BerechnungMultithreaded()
        for (i in 1..10000) {
            multi.berechneSpalte(cr = -0.4860438957475998,
                    ciMax = 0.2749190672153638,
                    aktuelleSpalte = 0,
                    zeilenzahl = 100,    // zusätzliche Schleife!
                    schrittI = -7.038961753141418E-4,
                    args = ApfelmännchenParameter(1000, StandardwerteEingabe.MAX_DISTANZ, StandardwerteEingabe.GRUNDFARBE, StandardwerteEingabe.FARB_ALGORITHMUS)
            )
        }
    }
}