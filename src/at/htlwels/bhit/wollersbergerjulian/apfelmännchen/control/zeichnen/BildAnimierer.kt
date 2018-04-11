package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control.zeichnen

import javafx.animation.AnimationTimer
import javafx.scene.image.WritableImage
import java.util.concurrent.ConcurrentLinkedQueue


// Created by julian on 06.04.18.
/** Dieser AnimationTimer nimmt die Punkte, die die
 * Threads berechnen und schreibt sie in das WritableImage.
 * Das Image kann aus dieser Klasse erhalten werden.
 *
 * Derjenige, der das Bild hat, muss eine
 * Referenz auf den BildAnimierer halten. */
class BildAnimierer(
        writableImage: WritableImage,
        /** Queue von Listn von einer Spalte*/
        private val queue: ConcurrentLinkedQueue<IntArray>
) : AnimationTimer() {

    val pixelWriter = writableImage.pixelWriter

    // Jeden Frame zeichne alle fertigen Spalten
    override fun handle(now: Long) {
        var arrr: IntArray? = queue.poll()

        while(arrr != null) {
            val spalte = arrr[0]

            for(i in 0..arrr.size-2) {
                pixelWriter.setArgb(spalte, i, arrr[i+1])
            }
            arrr = queue.poll()
        }
    }
}