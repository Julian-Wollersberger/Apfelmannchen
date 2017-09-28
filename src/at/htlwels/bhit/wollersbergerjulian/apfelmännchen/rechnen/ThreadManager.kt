package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen

import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

// Created by julian on 31.08.17.
/**
 * Mit dieser Klasse kann Code in einem anderen
 * Thread ausgeführt werden.
 *
 * Am einfachsten mit [berechne].
 * Für die benötigte Verzögerung beim Zoomen siehe
 * [berechneVerzögert].
 *
 * Die Threads sind in einem statischen ThreadPool,
 * sodass es global nie mehr als spezifiziert
 * gleichzeitig geben kann. Der Wert wird
 * von der Eingabe initialisiert.
 *
 * Von einem anderen Thread dürfen JavaFx-Elemente
 * nicht verändert werden. Dafür muss
 * Platform.runLater{} verwendet werden.
 */
class ThreadManager {

    companion object {
        val STANDARD_ANZAHL_THREADS = 4

        /** Ein ThreadPool, dessen Threads niedrige Priorität haben
         * und das Programm nicht am beenden hindern.
         *
         * Weil der PC nur soundso viele Threads hat,
         * macht es Sinn, dass es nur einen statischen
         * ThreadPool gibt.*/
        private val executor = ScheduledThreadPoolExecutor(STANDARD_ANZAHL_THREADS, LowPriorityThreadFactory())

        /** Beauftragt einen Thread aus dem ThreadPool mit
         * dem Berechnen.
         * Solange ein Thread frei ist, fängt dieser sofort an. */
        fun berechne(runnable: Runnable) {
            executor.execute(runnable)
        }

        /** Damit der Wert aus der Eingabe verwendet werden kann.
         * Wird im EingabeController verwendet. */
        fun setCorePoolSize(anzahlThreads: Int) {
            executor.corePoolSize = anzahlThreads
        }
    }

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
            executor.schedule({
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



    /**Erzeugt Threads, wenn diese benötigt werden.
     * Sie laufen mit niedriger Priorität und verhindern
     * nicht das beenden des Prozesses (Deamon).
     *
     * Kopiert aus [java.util.concurrent.Executors.DefaultThreadFactory]
     *
     * Ich habe die Priorität und isDeamon=true geändert.
     */
    internal class LowPriorityThreadFactory : ThreadFactory {
        private val group: ThreadGroup
        private val threadNumber = AtomicInteger(1)
        private val namePrefix: String

        init {
            val s = System.getSecurityManager()
            group = if (s != null)
                s.threadGroup
            else
                Thread.currentThread().threadGroup
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-"
        }

        override fun newThread(r: Runnable): Thread {
            val t = Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0)
            if (!t.isDaemon)
                t.isDaemon = true
            if (t.priority != Thread.MIN_PRIORITY)
                t.priority = Thread.MIN_PRIORITY
            return t
        }

        companion object {
            private val poolNumber = AtomicInteger(1)
        }
    }
}