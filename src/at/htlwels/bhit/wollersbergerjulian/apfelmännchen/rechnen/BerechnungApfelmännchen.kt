package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.IterationsWerte
import javafx.scene.paint.Color
import java.util.*

// Created by julian on 25.07.17.
/**
 * Hier stehen die Funktionen zum Berechnen der
 * Punkte der Mandelbrotmenge.
 *
 * #Wichtigste Funktionen:
 * [istInMenge] Der Iterations-Algorithmus für jeden Punkt
 * [berechneBereich] Schleife, um alle Punkte in einem (Bild)Bereich zu berechnen
 * [berechneFarbe] Eine schöne Farbe für einen Punkt basierend auf der Anzahl der
 * benötigten Iterationen.
 *
 * #Hilfsfunktionen:
 * [colorToArgbInt] Ein Color-Objekt bzw. drei Farb-Werte werden zu einem
 * Integer umgewandelt, wie es ein WriteableImage braucht.
 * [alleIterationen] Eine LinkedList mit allen Iterationsschritten
 * der Berechnung.
 * [zeichneIterationenFürPunkt] Zeichnet für den übergebenen Pixel-Punkt alle Schritte
 * des Iterations-Algorithmus.
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
fun istInMenge(cr: Double, ci: Double, maxIter: Int, maxDistanz: Double): Int {
    var zr = cr
    var zi = ci
    var zrtemp: Double
    // Distanz wird mit Pythagoras berechnet: dist² = zr² + zi²
    // Da Wurzel berechnen langsam ist, wird stattdessen der Vergleichswert quadriert.
    val maxDistanzQuad = maxDistanz * maxDistanz

    var i: Int = 0
    while (i < maxIter && zr*zr + zi*zi < maxDistanzQuad) {
        zrtemp = zr * zr - zi * zi + cr
        zi = 2.0 * zr * zi + ci
        zr = zrtemp
        i++
    }

    return i
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
 * @param args Fasst maxIterationen, maxDistanz und grundfarbe zusammen.
 * @param zeichnePunkt Mit dieser Funktion wird jeder Punkt gezeichnet.
 */
fun berechneBereich(
        koordsys: DoppelKoordinatenSystem,
        args: ApfelmännchenParameter,
        zeichnePunkt: (x: Int, y: Int, argb: Int) -> Unit
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

            val iter = istInMenge(cr, ci, args.maxIterationen, args.maxDistanz)
            val farbe = berechneFarbe(iter, args.maxIterationen, args.grundfarbe)
            zeichnePunkt(i, j, farbe)

            // Debug: Beim Koordinatensystem rudimentäre Achsen zeichnen:
            /*if(Math.abs(cr) <0.003 || Math.abs(ci) <0.003)
                arg.zeichnePunkt(i, j, colorToArgbInt(Color.BLACK))*/
        }
    }
}

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
 * @param grundfarbe Die Farbe, die der Mittelteil bekommt.
 * @return Die Farbe als Int im argb-Format
 */
fun berechneFarbe(iterationen: Int, maxIterationen: Int, grundfarbe: Color): Int {
    @Suppress("NAME_SHADOWING")
    var iterationen = iterationen
    /** Die Farbe als Int im argb-Format */
    val color: Int
    var runde: Int
    val fraction: Double

    if (iterationen == maxIterationen)
        color = colorToArgbInt(grundfarbe)
    else {
        /* Exponentielles Verhalten. Der HSV-Farbkreis wird sozusagen
         * einmal rundumgegangen mit den ersten 15 Iterationen. Für die
         * zweite Runde werden 31 Iterationen benötigt, für die Dritte
         * noch mal doppelt so viele. */
        iterationen += 8
        runde = 15
        while (iterationen >= runde)
        // Bitmuster ist immer nur einsen, somit kein Problem mit MAX_INT >= runde.
            runde = runde * 2 + 1
            //runde = runde * 4 + 3

        /* iterationen ist nun zwischen runde/2 und runde.
         * Deshalb zuerst minus runde/2; dadurch ist es zwischen 0 und runde/2.
         * Und nur noch in den Bereich von 0 bis 1 bingen. */
        fraction = (iterationen - runde / 2) / (runde / 2).toDouble()
        //fraction = (iterationen - runde / 4) / ((3*runde) / 4).toDouble()

        /* Die RGB-Werte folgen einem relativ einfachem Muster:
             * Es ist immer eine Farbe auf 255, eine auf 0 und die Dritte variabel.
             * Nach 60° ändert sich, welche. Siehe Farbauswahl bei GIMP.
             * Schritte:
             * 1. rot max,         grün 0,         blau wird mehr
             * 2. rot wird weniger,grün 0,         blau max
             * 3. rot 0,           grün wird mehr, blau max
             * 4. rot 0,           grün max,       blau wird weniger
             * 5. rot wird mehr,   grün max,       blau 0
             * 6. rot max,         grün weniger,   blau 0*/
        if (fraction < 0) {
            println("Fraction kleiner 0: $fraction iterationen: $iterationen runde: $runde")
            color = colorToArgbInt(grundfarbe)
        } else if (fraction < 1.0 / 6)
            color = colorToArgbInt(1.0, 0.0, fraction * 6, 1.0)
        else if (fraction < 2.0 / 6)
            color = colorToArgbInt(1 - (fraction - 1.0 / 6) * 6, 0.0, 1.0, 1.0)
        else if (fraction < 3.0 / 6)
            color = colorToArgbInt(0.0, (fraction - 2.0 / 6) * 6, 1.0, 1.0)
        else if (fraction < 4.0 / 6)
            color = colorToArgbInt(0.0, 1.0, 1 - (fraction - 3.0 / 6) * 6, 1.0)
        else if (fraction < 5.0 / 6)
            color = colorToArgbInt((fraction - 4.0 / 6) * 6, 1.0, 0.0, 1.0)
        else if (fraction <= 6.0 / 6)
            color = colorToArgbInt(1.0, 1 - (fraction - 5.0 / 6) * 6, 0.0, 1.0)
        else {
            println("Fraction größer 1: $fraction iterationen: $iterationen runde: $runde")
            color = colorToArgbInt(grundfarbe)
        }
    }
    return color
}

/** Kopiert aus WriteableImage.
* Wandelt ein Color-Objekt in einen Integer-Wert,
* wie es image.pixelWriter.setArgb brauch.*/
fun colorToArgbInt(c: Color): Int {
    val a = Math.round(c.opacity * 255).toInt()
    val r = Math.round(c.red * 255).toInt()
    val g = Math.round(c.green * 255).toInt()
    val b = Math.round(c.blue * 255).toInt()
    return (a shl 24 or (r shl 16) or (g shl 8) or b)
}

/** Kopiert und angepasst aus WriteableImage.
* Wandelt vier Farb-Double-Werte in einen Integer-Wert,
* wie es image.pixelWriter.setArgb brauch.*/
fun colorToArgbInt(red: Double, green: Double, blue: Double, opacity: Double): Int {
    val a = Math.round(opacity * 255).toInt()
    val r = Math.round(red * 255).toInt()
    val g = Math.round(green * 255).toInt()
    val b = Math.round(blue * 255).toInt()
    return (a shl 24 or (r shl 16) or (g shl 8) or b)
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

    return liste
}

/**
 * Zeichnet für den übergebenen Pixel-Punkt (px|py) alle Schritte
 * des Iterations-Algorithmus. Siehe [istInMenge]
 * Die Farbe wird mit dem entsprechenden Iterations-Schritt
 * und [berechneFarbe] ermittelt.
 *
 * Wenn ein Schritt auserhalb des Bildes ist, wird er
 * einfachnicht angezeigt.
 */
fun zeichneIterationenFürPunkt(
        px: Double, py: Double,
        koordsys: DoppelKoordinatenSystem,
        args: ApfelmännchenParameter,
        zeichnePunkt: (x: Int, y: Int, argb: Int) -> Unit
) {
    // Zuerst alles berechnen
    val liste = alleIterationen(koordsys.breiteToKX(px), koordsys.höheToKY(py),
            args.maxIterationen, args.maxDistanz)

    // Und dann alles zeichnen
    for (werte in liste) {
        try {
            zeichnePunkt(koordsys.kxToBreite(werte.zr).toInt(), koordsys.kyToHöhe(werte.zi).toInt(),
                    berechneFarbe(werte.iteration, args.maxIterationen, args.grundfarbe))
        } catch(e: IndexOutOfBoundsException) {
        }
    }
}