package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.figur

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter


// Created by julian on 21.07.18.
/**
 * Der Algorithmus für das Apfelmännchen.
 * @see istInMenge
 */
class ApfelmännchenFigur: Figur() {

    /**Diese Iteration ist das **Herzstück der Mandelbrotmenge**.
    * Für einen komplexen Punkt c wird ermittelt, ob diese Folge
    * gegen unendlich oder gegen 0 geht:
    *
    * + z(0) = c
    * + z(n) = (z(n-1))² + c
    *
    * Da ein Computer nicht unendlich viel Rechenleistung hat, kann nicht exakt
    * ermittelt werden, ob ein Punkt wirklich gegen 0 oder unendlich geht.
    *
    * Deshalb wird ein Näherungsverfahren eingesetzt:
    *
    * Sobald der Betrag (Distanz; |z|) nach vielen Iterationen >2 (?), kann es nicht mehr gegen
    * 0 gehen. Aber genau am Rand braucht es unendlich viele Iterationen, bis man entscheiden kann. Deshalb
    * wird eine maximale Anzahl an Iterationen verwendet; wenn es nach maxIterationen nicht klar ist, dann wird
    * angenommen, die Folge geht gegen 0.
    *
    * @param cr Realteil des Anfangspunktes
    * @param ci Imaginärteil des Anfangspunktes
    * @param maxIter Anzahl Iterationen, bis angenommen wird, die Folge geht gegen 0.
    * Je höher, desto genauer an den Rändern.
    * @param maxDistanz Betrag von z, ab dem die Folge gegen unendlich geht. Muss mindestens
    * 2 sein; höhere Werte liefern genauere Farben weit außen.
    * @return Anzahl Iterationen, bis die Folge maxDistanz überschritten hat.
    * Plus ein Wert zwischen 0 und 1, wieviel maxDistanz überschritten wurde.
    *
    * Wertebereich: 0 <= ret < maxIterationen+1
    *
    * Daraus wird die Farbe errechnet.
    */
    override fun istInMenge(cr: Double, ci: Double, maxIter: Int, maxDistanz: Double): Double {
        var zr = cr
        var zi = ci
        var zrtemp: Double
        // Distanz wird mit Pythagoras berechnet: dist² = zr² + zi²
        // Da Wurzel berechnen langsam ist, wird stattdessen der Vergleichswert quadriert.
        val maxDistanzQuad = maxDistanz * maxDistanz

        // Die magische Schleife!
        var i: Int = 0
        while (i < maxIter && zr*zr + zi*zi < maxDistanzQuad) {
            zrtemp = zr * zr - zi * zi + cr
            zi = 2.0 * zr * zi + ci
            zr = zrtemp
            i++
        }

        /* Für die Berechnung der Farbe ist die Iterationenzahl nicht stufenlos genug.
         * Die derzeitige Distanz ist nach der Schleife größer gleich maxDistanz.
         * --> Disivion ergibt Wert <= 1 */
        var feinjustierung: Double = maxDistanzQuad / (zr*zr + zi*zi)
        // Begrenzen zwischen 0 und 0.9999
        feinjustierung = Math.max(Math.min(feinjustierung, 0.9999), 0.0)

        return i + feinjustierung
    }

    override fun toString(): String {
        return "Apfelmännchen"
    }
}