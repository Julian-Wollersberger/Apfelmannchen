package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.zeichenfläche

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.BerechnungsParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.zeichneIterationenFürPunkt
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.HauptfensterController
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.Standardwerte
import javafx.application.Platform
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.input.MouseEvent

// Created by julian on 19.08.17.
/**
 * Hier werden alle Iterationen des einen Punktes angezeigt,
 * über dem die Maus steht.
 *
 * Diese Region ist nicht zoombar, sondern
 * die Grenzen ändern sich nicht.
 *
 * Der Code ist ziehmlich 1:1 aus SimpleZeichenRegion kopiert.
 */
class PunktZeichenregion(
        private val controller: HauptfensterController
) : ZeichenRegion() {

    /** Berechnet das Bild neu.
     * Dazu werden die Eingaben ausgelesen,
     * die Maße aktualisiert. */
    fun zeichneIterationen(event: MouseEvent) {
        // Werte aus TextFields holen
        val gelesenerBereich = controller.leseBereich()
        val maxIterationen = controller.leseMaxIterationen()

        /* Mit den aktuellen Werten ein neues entzerrtes Koordsys berechnen. */
        val koordsys = DoppelKoordinatenSystem(gelesenerBereich, getContentWidth(), getContentHeight()).entzerre()

        // Das Bild und die Parameter dazu
        val image = WritableImage(koordsys.breite.toInt(), koordsys.höhe.toInt())
        val pixelWriter = image.pixelWriter
        val params = BerechnungsParameter(koordsys.kBereich, koordsys.breite.toInt(), koordsys.höhe.toInt(),
                maxIterationen, Standardwerte.DISTANZ, Standardwerte.GRUNDFARBE,
                pixelWriter::setArgb)

        // Bild berechnen
        zeichneIterationenFürPunkt(params, koordsys, event.x, event.y)

        // Mach ein neues ImageView
        children.clear()
        val view = ImageView(image)
        children.add(view)
    }

    override fun reset() {
    }

    /** Das Kind sollte genauso groß sein, wie die Eltern. */
    override fun layoutChildren() {
        for(child in children) {
            child.resizeRelocate(layoutX +insets.top, layoutY +insets.left,
                    getContentWidth(), getContentHeight())
        }
    }
}
