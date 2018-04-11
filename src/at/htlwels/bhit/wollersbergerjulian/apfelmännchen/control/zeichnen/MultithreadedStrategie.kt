package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control.zeichnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.*
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.layout.Pane
import java.util.concurrent.ConcurrentLinkedQueue


// Created by julian on 20.12.17.
/**
 * Das Berechnen des Apfelmännchens wird auf
 * Threads aufgeteilt und was schon berechnet ist
 * wird live vom BildAnimierer auf das writeableImage geschrieben.
 */
class MultithreadedStrategie(
        elternPane: Pane,
        verwalter: ZeichenStrategienVerwalter
) : ZeichenStrategie(elternPane, verwalter) {

    private var animierer: BildAnimierer? = null
    private val berechnungMultithreaded = BerechnungMultithreaded()

    override fun aktualisiere() {
        try {
            berechneAnimiert(verwalter.koordsys, verwalter.parameter)
        } catch (e: IllegalArgumentException) {
            // On startup size is 0. The Image doesn't like that.
            //println("(Ignore on startup:) "+ e.localizedMessage)
        }
    }

    /** Das Berechnen des Apfelmännchens wird auf
     * Threads aufgeteilt und was schon berechnet ist
     * wird live vom BildAnimierer auf das writeableImage geschrieben.
     * Der BildAnimierer wird schon hier gestartet. */
    fun berechneAnimiert(
            koordsys: DoppelKoordinatenSystem,
            args: ApfelmännchenParameter
    ) {
        animierer?.stop()
        berechnungMultithreaded.interrupt()

        /* Remove the ones that are too old,
        * but keep some, so that the screen doesn't turn white. */
        if(zeichenPane.children.size >= 10)
            zeichenPane.children.remove(0, 5)
        val writableImage = WritableImage(koordsys.breite.toInt(), koordsys.höhe.toInt())
        zeichenPane.children.add(ImageView(writableImage))

        /* Die meiste Magie!*/
        berechnungMultithreaded.teileAufThreadsAuf(verwalter.anzahlThreads, koordsys, args)

        val animi = BildAnimierer(writableImage, berechnungMultithreaded.queue)
        animi.start()
        animierer = animi
    }
}