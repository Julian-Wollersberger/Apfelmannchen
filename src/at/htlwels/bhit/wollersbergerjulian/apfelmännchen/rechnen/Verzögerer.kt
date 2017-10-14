package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen

import javafx.util.Callback
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

// Created by julian on 05.10.17.
/** Mit jeder Eingabe des Nutzers wird die vorherige
 * geschedulte Berechnung abgebrochen, wenn die 200ms
 * noch nicht abgelaufen sind.
 * Wenn der Nutzer unendlich lange durchgehend scrollt,
 * tut sich einfach gar nichts.
 * Ist glaub ich nicht schlimm.
 */
class Verzögerer {

    private var letzteScheduledFuture: ScheduledFuture<*>? = null

    /**Startet die Berechnung verzögert.
     * Für das Zoomen konzipiert, damit das Mausrad ein paar
     * mal gedreht werden kann, ohne das es dabei bei jedem
     * gedrehten Millimeter eine Berechnung startet.
     *
     * Wenn diese Funktion aufgerufen wird, wenn das
     * geschedulte des letzten Aufrufs noch nicht
     * ausgeführt wird, so wird jenes abgebrochen
     * und das neue geschedult. */
    fun berechneVerzögert(delay: Long, berechnung: Runnable) {
        /* Bricht das letzte ab, wenn
         * es noch nicht am Ausführen oder fertig ist. */
        val letzte = letzteScheduledFuture
        if(letzte != null)
            letzte.cancel(false)

        // Gibt die neue Berechnung in Auftrag.
        letzteScheduledFuture = GlobalerThreadManager.schedule(
                berechnung, delay, TimeUnit.MILLISECONDS)
    }

    companion object {
        //TODO Verzögerung zur Eingabe!
        public val standardVerzögerung: Long = 200L
    }
}