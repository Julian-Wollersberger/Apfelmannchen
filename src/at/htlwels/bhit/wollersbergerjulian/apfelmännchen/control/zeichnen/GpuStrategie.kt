package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control.zeichnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.GpuBeschleuniger
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.layout.Pane


// Created by julian on 08.07.18.
/**
 * TODO Description
 */
internal class GpuStrategie(
        elternPane: Pane,
        val verwalter: ZeichenStrategienVerwalter
) : ZeichenStrategie(elternPane) {

    val beschleuniger = GpuBeschleuniger()

    override fun aktualisiere() {
        try {
            val koordsys = verwalter.koordsys
            val writableImage = WritableImage(koordsys.breite.toInt(), koordsys.höhe.toInt())

            beschleuniger.berechneBild(koordsys, verwalter.parameter, writableImage.pixelWriter::setArgb)

            super.zeichenPane.children.clear()
            super.zeichenPane.children.add(ImageView(writableImage))

        } catch (e: IllegalArgumentException) {
            // Beim Initialisieren tritt dieser Fehler auf:
            // Image dimensions must be positive (w,h > 0)
        }
    }
}