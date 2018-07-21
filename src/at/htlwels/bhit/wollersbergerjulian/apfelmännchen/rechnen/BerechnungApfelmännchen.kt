package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.IterationsWerte
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.farbe.FarbAlgorithmus
import java.util.*

// Created by julian on 25.07.17.
/**
 * Hier stehen die Funktionen zum Berechnen der
 * Punkte der Mandelbrotmenge.
 *
 * #Wichtigste Funktionen:
 * [istInMenge] Der Iterations-Algorithmus für jeden Punkt
 * [berechneBereich] Alle Punkte im Bereich;
 * Wurde verschoben nach [BerechnungAnimiert]
 * [berechneFarbe] Eine schöne Farbe für einen Punkt basierend auf der Anzahl der
 * benötigten Iterationen.
 *
 * #Hilfsfunktionen:
 * [colorToArgbInt] Ein Color-Objekt bzw. drei Farb-Werte
 * werden zu einem Integer umgewandelt, wie es
 * ein WriteableImage braucht.
 * [alleIterationen] Eine LinkedList mit allen Iterationsschritten
 * der Berechnung.
 * [zeichneIterationenFürPunkt] Zeichnet für den übergebenen
 * Pixel-Punkt alle Schritte des Iterations-Algorithmus.
 * [alleIterationen] Gibt eine Liste aller Iterationen
 * für einen Punkt zurück.
 *
 */








/**
 * Siehe [istInMenge]
 *
 * @return Eine LinkedList mit allen Iterationsschritten
 * der Berechnung. iteration geht von 0 bis maxIter-1
 */
@Deprecated("Diese Datei wird irgendwann gelöscht.")
fun alleIterationen(cr: Double, ci: Double, maxIter: Int, maxDistanz: Double): LinkedList<IterationsWerte> {
    val liste = LinkedList<IterationsWerte>()
    var zr = cr
    var zi = ci
    var zrtemp: Double
    val maxDistanzQuad = maxDistanz * maxDistanz

    var i: Int = 0
    while (i < maxIter && zr*zr + zi*zi < maxDistanzQuad) {
        zrtemp = zr * zr - zi * zi + cr
        zi = 2.0 * zr * zi + ci
        zr = zrtemp

        liste.add(IterationsWerte(zr, zi, i))
        i++
    }

    val feinjustierung = maxDistanzQuad / (zr*zr + zi*zi)
    println("Feinjustierung: "+ Math.min(1.0, Math.max(0.0, Math.log10(1.0+ 9*feinjustierung) )))
    return liste
}
