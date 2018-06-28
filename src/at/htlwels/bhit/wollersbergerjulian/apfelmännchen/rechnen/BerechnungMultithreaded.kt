package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import java.util.concurrent.ConcurrentLinkedQueue

// Created by julian on 06.10.17.

class BerechnungMultithreaded {

    private val threadQueue = ConcurrentLinkedQueue<Thread>()
    val queue = ConcurrentLinkedQueue<IntArray>()

    fun teileAufThreadsAuf(
            anzahlThreads: Int,
            koordsys: DoppelKoordinatenSystem,
            args: ApfelmännchenParameter
    ) {
        // Berechnung auf Threads aufteilen
        for (i in 0..anzahlThreads - 1) {
            val thread = Thread(Runnable {
                berechneTeilBereich(
                        i, anzahlThreads,
                        koordsys, args, queue)
            })
            thread.start()
            threadQueue.add(thread)
        }
    }

    fun interrupt() {
        threadQueue.forEach { it.interrupt() }
        threadQueue.clear()
    }

    fun waitForAll() {
        threadQueue.forEach { it.join() }
        threadQueue.clear()
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
        val spaltenzahl = koordsys.breite.toInt() - 1
        val zeilenzahl = koordsys.höhe.toInt() - 1
        val schrittR: Double = (koordsys.kxMax - koordsys.kxMin) / spaltenzahl
        val schrittI: Double = -(koordsys.kyMax - koordsys.kyMin) / zeilenzahl

        var i = offset
        while (i < spaltenzahl && !Thread.currentThread().isInterrupted) {
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
        val punktFarben = IntArray(zeilenzahl + 1)
        punktFarben[0] = aktuelleSpalte

        var ci = ciMax
        for (j in 0..(zeilenzahl - 1)) {
            // Kein += damit weniger Rundungsfehler
            ci = ciMax + schrittI * j

            val farbe = istInMenge(cr, ci, args)
            punktFarben[j + 1] = farbe

            // Debug: Beim Koordinatensystem rudimentäre Achsen zeichnen:
            /*if(Math.abs(cr) <0.003 || Math.abs(ci) <0.003)
            arg.zeichnePunkt(i, j, colorToArgbInt(Color.BLACK))*/
        }

        return punktFarben
    }
}