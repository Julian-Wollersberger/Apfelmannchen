package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem


/**
 * Created by julian on 03.10.17.
 */

fun berechneBereich(){} //? Brauchts das noch ?

//Oder ThreadManager?
fun berechneBereichMultiThreaded(
        anzahlThreads: Int,
        koordsys: DoppelKoordinatenSystem,
        args: ApfelmännchenParameter,
        zeichnePunkt: (x: Int, y: Int, argb: Int) -> Unit
) {
    for (i in 0..anzahlThreads-1) {
        Thread {
            berechneTeilBereich(i, anzahlThreads,
                    koordsys, args, zeichnePunkt)
        }.start()
    }
}

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
private fun berechneTeilBereich(
        offset: Int, jedeWievielteSpalte: Int,
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

    var i = offset
    //for (i in offset..(spaltenzahl-1)) {
    while (i < spaltenzahl) {
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
        i += jedeWievielteSpalte
    }
}
private fun berechneSpalte(){}

/** Hier Funktionen, die ein WriteableImage bzw.
 * ein Array davon zurückgeben, bzw. ein <?(Writable Image) extends Runnable> aufrufen.*/