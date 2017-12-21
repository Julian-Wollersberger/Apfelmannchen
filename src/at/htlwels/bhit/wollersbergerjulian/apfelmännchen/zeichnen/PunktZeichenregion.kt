package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.zeichnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.zeichneIterationenFürPunkt
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control.ZeichenflächeController
import javafx.event.EventHandler
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
 * TODO Es soll das koordsys von InteraktiveZeichenRegion abfragen.
 *
 * Der Code ist ziehmlich 1:1 aus SimpleZeichenRegion kopiert.
 *
 * TODO Punkt-Stärke soll mehr als 1 Pixel sein.
 */
@Deprecated("Benutze die Strategien.")
class PunktZeichenregion(
        private val controller: ZeichenflächeController
) : ZeichenRegion() {
    private val eingaben = controller.eingaben


    override fun registerEventHanders() {
        controller.addEventHandler(MouseEvent.MOUSE_MOVED, EventHandler { zeichneIterationen(it) })
    }

    override fun berechneBild() {
    }

    /** Berechnet das Bild neu.
     * Dazu werden die Eingaben ausgelesen,
     * die Maße aktualisiert. */
    fun zeichneIterationen(event: MouseEvent) {

        // Werte aus der Eingabe holen
        val gelesenerBereich = eingaben.eingabeBereich
        val params = ApfelmännchenParameter(
                eingaben.eingabeMaxIterationen,
                eingaben.eingabeMaxDistanz,
                eingaben.eingabeGrundfarbe
        )

        /* Mit den aktuellen Werten ein neues entzerrtes Koordsys berechnen. */
        val koordsys = DoppelKoordinatenSystem(gelesenerBereich, getContentWidth(), getContentHeight()).entzerre()

        // Das Bild
        val image = WritableImage(koordsys.breite.toInt(), koordsys.höhe.toInt())
        val pixelWriter = image.pixelWriter

        // Bild berechnen
        zeichneIterationenFürPunkt(event.x, event.y, koordsys, params, pixelWriter::setArgb)

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
