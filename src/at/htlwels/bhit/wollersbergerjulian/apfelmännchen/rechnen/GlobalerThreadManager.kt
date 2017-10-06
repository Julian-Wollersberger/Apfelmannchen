package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen

import javafx.beans.value.ChangeListener
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

// Created by julian on 31.08.17.
/**TODO Beschreibung anpassen.
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
class GlobalerThreadManager {
    companion object {
        const val STANDARD_ANZAHL_THREADS = 4

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

        /** TODO Beschreibung */
        fun  schedule(function: () -> Unit, delay: Long, timeUnit: TimeUnit) {
            executor.schedule(function, delay, timeUnit)
        }

        /** Damit der Wert aus der Eingabe verwendet werden kann.
         * Wird im EingabeController verwendet. */
        fun setCorePoolSize(anzahlThreads: Int) {
            executor.corePoolSize = anzahlThreads
        }

        /** Die CorePoolSize sollte das sein, was in der
         * Eingabe steht. */
        fun getCorePoolSize(): Int {
            return executor.corePoolSize
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
}