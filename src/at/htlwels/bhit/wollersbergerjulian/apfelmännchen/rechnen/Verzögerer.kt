package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean


/**
 * Created by julian on 05.10.17.
 *
 * TODO Verzögerer vielleicht ändern?
 *
 * Wäre es nicht besser, die Verzögerung auf
 * ca. 200ms zu setzen und das mit JEDER
 * Eingabe zurückzusetzen?, Wenn der Nutzer
 * unendlich lange scrollt, tut sich einfach gar nichts.
 * Ist glaub ich nicht schlimm.
 */

//TODO Verzögerung auf 200ms bzw. zur Eingabe!
public const val standardVerzögerung: Long = 500L

class Verzögerer {

    /** True, wenn der Time läuft, aber wieder false, wenn der Thread läuft. */
    private val timerGestartet = AtomicBoolean(false)
    private var neustesRunnable = Runnable { }

    /**Startet die Berechnung verzögert.
     * Für das Zoomen konzipiert, damit das Mausrad ein paar
     * mal gedreht werden kann.
     *
     * Der Erste Aufruf startet den Timer.
     * Wird diese Funktion aufgerufen, während die Zeit
     * noch abläuft, wird das runnable ausgewechselt.
     * Wenn der Timer abgelaufen ist, wird das
     * runnable des letzten Aufrufs ausgeführt.
     * Der Aufruf dannach startet den Timer neu. */
    fun berechneVerzögert(delay: Long, runnable: Runnable) {
        neustesRunnable = runnable

        // Wenn kein Timer läuft
        if(timerGestartet.getAndSet(true) == false) {
            GlobalerThreadManager.schedule({
                /* Problem: Beginnt schon eine neue
                 * Berechnung, während die andere noch läuft.
                 * Wenn es nach neustesRunnable.run() ist, werden
                 * Aufrufe ignoriert, die während der Berechnung stattfinden. */
                timerGestartet.set(false)

                /* Es wird in dieser Lambda-Expr. der Wert
                 * von neustesRunnable  verwendet, der nach dem delay
                 * zum Zeitpunkt des Thread-Starts darin ist, und
                 * nicht zum Zeitpunkt des Aufrufs von schedule(). */
                neustesRunnable.run()
            }, delay, TimeUnit.MILLISECONDS)
        }
    }
}