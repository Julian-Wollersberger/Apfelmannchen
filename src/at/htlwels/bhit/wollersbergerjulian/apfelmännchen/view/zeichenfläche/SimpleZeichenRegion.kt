package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.zeichenfläche

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.BerechnungsParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.RechenThread
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.HauptfensterController
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.Standardwerte
import javafx.application.Platform
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage

/**Diese ZeichenRegion beinhaltet nur eine ImageView,
 * ohne Genauigkeitsklassen und das alles.
 *
 * Auf dem Image kann gezeichnet werden.
 */
class SimpleZeichenRegion(
        private val controller: HauptfensterController,
        private var koordsys: DoppelKoordinatenSystem
) : ZeichenRegion() {

    /** In dieses Image wird von einem anderen Thread geschrieben! */
    var concurrentImage = WritableImage(1, 1)

    /** Berechnet das Bild neu.
     * Dazu werden die Eingaben ausgelesen,
     * die Maße aktualisiert und ein
     * Thread erstellt, der die
     * Berechnung durchführt. */
    override fun reset() {
        // Werte aus TextFields holen
        val gelesenerBereich = controller.leseBereich()
        val maxIterationen = controller.leseMaxIterationen()

        /* Mit den aktuellen Werten ein neues entzerrtes Koordsys berechnen. */
        koordsys = DoppelKoordinatenSystem(gelesenerBereich, getContentWidth(), getContentHeight()).entzerre()

        // Das Bild und die Parameter dazu
        concurrentImage = WritableImage(koordsys.breite.toInt(), koordsys.höhe.toInt())
        val pixelWriter = concurrentImage.pixelWriter
        val params = BerechnungsParameter(koordsys.kBereich, koordsys.breite.toInt(), koordsys.höhe.toInt(),
                maxIterationen, Standardwerte.DISTANZ, Standardwerte.GRUNDFARBE,
                pixelWriter::setArgb)
        // Bild berechnen in neuem Thread.
        RechenThread(params, this::concurrentChangeImageView, concurrentImage, koordsys).start()
    }

    /** Macht ein neues ImageView aus dem
     * übergebenem Image.
     *
     * Es darf von einem beliebigen Thread aus
     * aufgerufen werden, weil es [Platform.runLater] aufruft.
     * Die Parameter werden hier ignoriert, weil sie nur beim zoomen gebraucht werden. */
    fun concurrentChangeImageView(image: WritableImage, imageKoordsys: DoppelKoordinatenSystem) {
        Platform.runLater {
            children.clear()
            val view = ImageView(concurrentImage)
            children.add(view)
        }
    }

    /** Das Kind sollte genauso groß sein, wie die Eltern. */
    override fun layoutChildren() {
        for(child in children) {
            child.resizeRelocate(layoutX +insets.top, layoutY +insets.left,
                    getContentWidth(), getContentHeight())
        }
    }
}