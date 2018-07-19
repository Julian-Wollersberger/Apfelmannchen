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

/**Diese Iteration ist das **Herzstück der Mandelbrotmenge**.
 * Für einen komplexen Punkt c wird ermittelt, ob diese Folge
 * gegen unendlich oder gegen 0 geht:
 * `
 * <br></br>     z(0) = c
 * <br></br>     z(n) = (z(n-1))² + c
` *
 * <br></br><br></br>
 * Da ein Computer nicht unendlich viel Rechenleistung hat, kann nicht exakt
 * ermittelt werden, ob ein Punkt wirklich gegen 0 oder unendlich geht.
 * <br></br>Deshalb wird ein Näherungsverfahren eingesetzt:
 * <br></br>Sobald der Betrag (Distanz; |z|) nach vielen Iterationen >2 (?), kann es nicht mehr gegen
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
 * Daraus wird die Farbe errechnet. Geht von 0 bis maxIterationen (inklusiv).
 */
fun istInMenge(cr: Double, ci: Double, args: ApfelmännchenParameter): Int {
    val maxIter: Int = args.maxIterationen
    var zr = cr
    var zi = ci
    var zrtemp: Double
    // Distanz wird mit Pythagoras berechnet: dist² = zr² + zi²
    // Da Wurzel berechnen langsam ist, wird stattdessen der Vergleichswert quadriert.
    val maxDistanzQuad = args.maxDistanz * args.maxDistanz

    var i: Int = 0
    while (i < maxIter && zr*zr + zi*zi < maxDistanzQuad) {
        zrtemp = zr * zr - zi * zi + cr
        zi = 2.0 * zr * zi + ci
        zr = zrtemp
        i++
    }

    // Für die Berechnung der Farbe ist die Iterationenzahl zu wenig.
    // Die derzeitige Sistanz ist nach der Schleife größer gleich maxDistanz. => Disivion ergibt <= 1
    val feinjustierung: Double = maxDistanzQuad / (zr*zr + zi*zi);
    return args.farbAlgorithmus.berechneFarbe(i, maxIter, args.grundfarbe, feinjustierung)
}


/*
* Dies ist die Formel für z³+c statt z²+c.
* Erzeugt ein anderes schönes Muster.
* https://youtu.be/qhbuKbxJsk8?t=5m52s
*
zrtemp = zr*zr*zr - 3*zr*zi*zi + cr;
zi = 3*zr*zr*zi - zi*zi*zi + ci;
zr = zrtemp;
* */

/**Berechnet jeden Punkt im Bereich des koordsys.
 * Das Bild, auf das gezeichnet wird, hat die
 * Größe breite*höhe.
 * Daraus ergibt sich die Schrittweite zwischen den
 * Punkten.
 * Mit maxIterationen kann man steuern, wie lange
 * die Berechnung dauert.
 *
 * Für jeden Punkt wird istInMenge ausgerechnet, und
 * mit der resultierenden Farbe dann gezeichnet.
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
    val spaltenzahl = koordsys.breite.toInt()
    val zeilenzahl = koordsys.höhe.toInt()
    //TODO Schrittweite berechnen in DoppelKoordsys hinein
    val schrittR: Double = (koordsys.kxMax - koordsys.kxMin) / spaltenzahl
    val schrittI: Double = -(koordsys.kyMax - koordsys.kyMin) / zeilenzahl

    for (i in 0..(spaltenzahl-1)) {
        // Kein += damit weniger Rundungsfehler
        cr = koordsys.kxMin + schrittR * i
        for (j in 0..(zeilenzahl-1)) {
            ci = koordsys.kyMax + schrittI * j

            doSomething(i, j, cr, ci)
        }
    }
}



/**
 * Siehe [istInMenge]
 *
 * @return Eine LinkedList mit allen Iterationsschritten
 * der Berechnung. iteration geht von 0 bis maxIter-1
 */
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
