package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import javafx.animation.AnimationTimer
import javafx.scene.image.PixelFormat
import javafx.scene.image.WritableImage
import java.nio.IntBuffer
import java.util.concurrent.ConcurrentLinkedQueue


/** TODO Austauschbar machen durch eigene Klassen und Interfaces.
 * Created by julian on 06.10.17.
 */

/** Das Berechnen des Apfelmännchens wird auf
 * Threads aufgeteilt und was schon berechnet ist
 * wird live auf das WritableImage im zurückgegebenen
 * BildAnimierer geschrieben.
 * Der BildAnimierer wird schon hier gestartet. */
fun berechneAnimiert(
        koordsys: DoppelKoordinatenSystem,
        args: ApfelmännchenParameter
) : BildAnimierer {

    val writableImage = WritableImage(koordsys.breite.toInt(), koordsys.höhe.toInt())
    val queue = ConcurrentLinkedQueue<IntArray>()
    val anzahlThreads = GlobalerThreadManager.getCorePoolSize()

    // Berechnung auf Threads aufteilen
    for (i in 0..anzahlThreads-1) {
        GlobalerThreadManager.berechne(Runnable {
            berechneTeilBereich(
                    i, anzahlThreads,
                    koordsys, args, queue)
        })
    }

    //TODO abbrechbar machen, dazu Threads merken.
    val animi = BildAnimierer(writableImage, queue)
    animi.start()
    return animi
}

/** Dieser AnimationTimer nimmt die Punkte, die die
 * Threads berechnen und schreibt sie in das WritableImage.
 * Das Image kann aus dieser Klasse erhalten werden.
 *
 * Derjenige, der das Bild hat, muss eine
 * Referenz auf den BildAnimierer halten. */
class BildAnimierer(
        public val writableImage: WritableImage,
        /** Queue von Listn von einer Spalte*/
        private val queue: ConcurrentLinkedQueue<IntArray>
) : AnimationTimer() {

    val pixelWriter = writableImage.pixelWriter

    override fun handle(now: Long) {
        var arrr: IntArray? = queue.poll()

        while(arrr != null) {
            val spalte = arrr[0]

            /*val pixelFormat: PixelFormat<IntBuffer> = PixelFormat.getIntArgbInstance()
            /* Der Buffer beginnt erst beim zweiten Element. */
            val buffer = IntBuffer.wrap(arrr, 1, arrr.size-1)

            // Ist x oder y Spalte?
            pixelWriter.setPixels(0, spalte, 400, 1, pixelFormat, buffer, 0)
            println(queue.size)*/

            for(i in 0..arrr.size-2) {
                pixelWriter.setArgb(spalte, i, arrr[i+1])
            }
            arrr = queue.poll()
        }
    }

}

/**Hier werden außerdem nur alle n-ten Spalten
 * berechnet. Alle anderen können in anderen
 * Aufrufen dieser Funktion in verschiedenen
 * Threads gemacht werden.
 *
 * Berechnet jeden Punkt im Bereich des koordsys.
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
 * @param jedeWievielteSpalte Es wird nur jede n-te Spalte berechnet.
 * @param offset Ob es mit der 1. oder 2. oder x-ten Spalte anfangen soll.
 * @param koordsys Dessen Bereich  wird gezeichnet
 * und dessen Breite und Höhe sind die Dimensionen des Bildes.
 * @param args Fasst maxIterationen, maxDistanz und grundfarbe zusammen.
 * @param queue Hierhinein kommen Arrays der berechneten Farbwerte
 * einer Spalte.
 */
private fun berechneTeilBereich(
        offset: Int, jedeWievielteSpalte: Int,
        koordsys: DoppelKoordinatenSystem,
        args: ApfelmännchenParameter,
        queue: ConcurrentLinkedQueue<IntArray>
) {
    var cr = koordsys.kxMin
    val spaltenzahl = koordsys.breite.toInt() -1
    val zeilenzahl = koordsys.höhe.toInt() -1
    val schrittR: Double = (koordsys.kxMax - koordsys.kxMin) / spaltenzahl
    val schrittI: Double = -(koordsys.kyMax - koordsys.kyMin) / zeilenzahl

    var i = offset
    while (i < spaltenzahl) {
        // Kein += damit weniger Rundungsfehler
        cr = koordsys.kxMin + schrittR * i

        // Jede Spalte zur Queue hinzufügen.
        val arrr = berechneSpalte(cr, koordsys.kyMax, i, zeilenzahl, schrittI, args)
        queue.add(arrr)

        i += jedeWievielteSpalte
    }
}

/**Berechnet alle Punkte einer Spalte.
 * @return ein IntArray, mit den ArgbInt-Farbwerten.
 * Im ersten Element steht, für welche
 * Spalte im Bild es ist.*/
internal fun berechneSpalte(
        cr: Double, ciMax: Double, aktuelleSpalte: Int, zeilenzahl: Int, schrittI: Double,
        args: ApfelmännchenParameter
): IntArray {
    val punktFarben = IntArray(zeilenzahl+1)
    punktFarben[0] = aktuelleSpalte

    var ci = ciMax
    for (j in 0..(zeilenzahl-1)) {
        // Kein += damit weniger Rundungsfehler
        ci = ciMax + schrittI * j

        val iter = istInMenge(cr, ci, args.maxIterationen, args.maxDistanz)
        val farbe = berechneFarbe(iter, args.maxIterationen, args.grundfarbe)
        punktFarben[j+1] = farbe

        // Debug: Beim Koordinatensystem rudimentäre Achsen zeichnen:
        /*if(Math.abs(cr) <0.003 || Math.abs(ci) <0.003)
            arg.zeichnePunkt(i, j, colorToArgbInt(Color.BLACK))*/
    }

    return punktFarben
}