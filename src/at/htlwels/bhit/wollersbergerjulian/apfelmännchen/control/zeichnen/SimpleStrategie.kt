package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control.zeichnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.figur.Figur
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.layout.Pane


// Created by julian on 20.12.17.
/**
 * Ohne jeden Schnickschnak. Ganz einfach ein
 * Image berechnen.
 */
internal class SimpleStrategie(
        elternPane: Pane,
        val verwalter: ZeichenStrategienVerwalter
) : ZeichenStrategie(elternPane) {

    /** Berechnet ein neues Bild im Fx Application Thread. */
    override fun aktualisiere() {
        try {
            val koordsys = verwalter.koordsys
            val args = verwalter.parameter
            val writableImage = WritableImage(koordsys.breite.toInt(), koordsys.höhe.toInt())

            Figur.fürJedenPunkt(koordsys, { x, y, cr, ci ->
                // Berechne die Farbe.
                val iterationen = args.figur.istInMenge(cr, ci, args.maxIterationen, args.maxDistanz)
                val farbe = args.farbAlgorithmus.berechneFarbe(iterationen, args.maxIterationen, args.grundfarbe)
                writableImage.pixelWriter.setArgb(x, y, farbe)
            })

            super.zeichenPane.children.clear()
            super.zeichenPane.children.add(ImageView(writableImage))

        } catch (e: IllegalArgumentException) {
            // Beim Initialisieren tritt dieser Fehler auf:
            // Image dimensions must be positive (w,h > 0)
        }

    }
}