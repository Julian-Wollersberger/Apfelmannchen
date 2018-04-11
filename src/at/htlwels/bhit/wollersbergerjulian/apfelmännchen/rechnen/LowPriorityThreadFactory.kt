package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger


// Created by julian on 11.04.18.
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
