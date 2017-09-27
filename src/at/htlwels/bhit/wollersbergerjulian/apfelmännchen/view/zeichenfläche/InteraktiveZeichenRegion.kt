package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.zeichenfläche

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.BerechnungsParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.RechenThread
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.HauptfensterController
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.Standardwerte
import javafx.application.Platform
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.input.ScrollEvent
import java.util.*

/**
 * TODO SimpleZeichenRegion, nur mit Events
 *
 */
class InteraktiveZeichenRegion(
        private val controller: HauptfensterController,
        private var koordsys: DoppelKoordinatenSystem
) : ZeichenRegion() {

    var image = WritableImage(1, 1)
    var imageView = ImageView()
    /** Das Koordsys, mit dem das aktuell in [imageView] gespeicherte
     * Bild berechnet wurde. */
    var imageKoordsys = koordsys
    /** Solange ein Thread rechnet, darf kein neuer erzeugt werden. */
    var threadBeimRechnen = false
    var ersterZoomZeitstempel: Long = -1L

    /**Diese Methode berechnet ein neues Bild mit den
     * Eingaben im Hauptfenster. */
    override fun reset() {
        // Werte aus TextFields holen
        koordsys.kBereich = controller.leseBereich()
        val maxIterationen = controller.leseMaxIterationen()

        /* Mit den aktuellen Werten ein neues entzerrtes Koordsys berechnen. */
        koordsys = DoppelKoordinatenSystem(koordsys.kBereich, getContentWidth(), getContentHeight()).entzerre()

        berechneBild(maxIterationen, getContentWidth().toInt(), getContentHeight().toInt())
    }

    /**Beauftragt einen neuen Thread, der
     * das Bild berechnet.
     * TODO Eine Parameter-Klasse erstellen und hier eine Methode, die diese erzeugt */
    fun berechneBild(maxIterationen: Int, w: Int, h: Int) {
        if (threadBeimRechnen == false) {
            threadBeimRechnen = true
            // Das Bild und die Parameter dazu.
            val neuesImage = WritableImage(w, h)
            val pixelWriter = neuesImage.pixelWriter
            val params = BerechnungsParameter(koordsys.kBereich, w, h, maxIterationen,
                    Standardwerte.DISTANZ, Standardwerte.GRUNDFARBE,
                    pixelWriter::setArgb)
            // Bild berechnen in neuem Thread.
            RechenThread(params, this::concurrentChangeImageView,
                    neuesImage, koordsys.copy()).start()
        }
    }
    /** Macht ein neues ImageView aus dem
     * übergebenem Image. imageKoordsys wird aktualisiert.
     *
     * Es darf von einem beliebigen Thread aus
     * aufgerufen werden, weil es [Platform.runLater] aufruft. */
    fun concurrentChangeImageView(neuesImage: WritableImage, imageKoordsys: DoppelKoordinatenSystem) {
        Platform.runLater {
            image = neuesImage
            this.imageKoordsys = imageKoordsys
            val view = ImageView(neuesImage)
            imageView = view

            children.clear()
            children.add(view)

            /* Der Thread ist fertig. Aber wenn zwischenzeitlich
            * gezoomt wurde, kann er schon wieder anfangen. */
            threadBeimRechnen = false
            ersterZoomZeitstempel = -1L
            /*if(koordsys != imageKoordsys)
                berechneBild(controller.leseMaxIterationen(), koordsys.breite.toInt(), koordsys.höhe.toInt())*/
        }
    }

    /**Beim Zoomen soll der kartesische Punkt, über dem die Maus ist,
     * vorher und nachher gleich bleiben.
     *
     * Runterscrollen -> hineinzoomen
     *
     * Nach dem Zoomen sind der Bereich, V und scale
     * verändert; sie repräsentieren die neue Ansicht.
     * Das berechnete Bild passt nicht mehr dazu.
     */
    fun zoomeBeimScrollen(event: ScrollEvent) {
        val zoomFaktor = 1 - event.deltaY/120

        koordsys = koordsys.erzeugeKartesischenAusschnitt(zoomFaktor, event.x, event.y)

        // Das ruft [layoutChildren] auf, damit das ImageView angepasst werden kann.
        requestLayout()

        /* Es fängt nicht sofort nach dem ersten Mausrad-Dreher
         * zum berechnen an, sondern wartet eine halbe Sekunde, ob
         * noch mehr gezoomt wird.*/
        if(ersterZoomZeitstempel == -1L) {
            ersterZoomZeitstempel = System.currentTimeMillis()
            // Nach spätestens einer Sekunde aber soll berechnet werden
            Timer(true).schedule(object : TimerTask() {
                override fun run() {
                    Platform.runLater{
                        berechneBild(controller.leseMaxIterationen(),
                                getContentWidth().toInt(), getContentHeight().toInt())
                    }
                }
            }, 500L)
        }

        /* Erst wenn eine Sekunde vergangen ist, seitdem das
        * erste mal gezoomt wurde, soll neu berechnet werden.*/
        if((System.currentTimeMillis() - ersterZoomZeitstempel) >= 1000L)
            berechneBild(controller.leseMaxIterationen(),
                    getContentWidth().toInt(), getContentHeight().toInt())
                    //koordsys.breite.toInt(), koordsys.höhe.toInt())

        //TODO ThreadPool oder so was, das entscheidet, wann und ob berechnet wird.
    }

    /** Kind soll so groß sein wie Elter.
     * ImageView ist allerdings nicht resizeable,
     * deshalb muss es gezoomt werden.
     *
     * TODO Das Zoomen beim Layouten will einfach nicht funktionieren :(
     * Ich schaffe es nicht, eine Formel zu machen... */
    override fun layoutChildren() {
        // Child sollte das ImageView sein.
        for(child in children) {
            /* Das hier beeinflusst die LayoutBounds.
            * Die Verschiebung weiter unten wird dannach berechnet. */
            child.resizeRelocate(layoutX +insets.top, layoutY +insets.left,
                    getContentWidth(), getContentHeight())

            /* Was will ich hier?
            *
            * + scale herausfinden aus koordsys und imageKoordsys.
            * + das imageView muss so skaliert und verschoben werden,
            *   dass die beiden koordsys aufeinander passen.
            * */

            // Wenn koordsys reingezoomt ist, dann Bsp: 2 = 2.4 / 1.2*/
            // Funktioniert.
            child.scaleX = imageKoordsys.kxSpanne / koordsys.kxSpanne
            child.scaleY = imageKoordsys.kySpanne / koordsys.kySpanne

            //Die Verschiebung durch Scale muss rückgängig gemacht werden.
            println("Local  "+ child.boundsInLocal)
            println("Parent "+ child.boundsInParent)
            // Verschiebung der V. Bild ist Anfang.
            child.layoutX = (koordsys.kxMin - imageKoordsys.kxMin)*koordsys.scaleBreite
                    //koordsys.kxToBreite(imageKoordsys.kxMin)// *child.scaleX
                    //- imageKoordsys.kxToBreite(koordsys.kxMin) *child.scaleX
                    //koordsys.Vb - imageKoordsys.Vb
                    + child.layoutBounds.width*(child.scaleX-1)/2       //Verschiebung durch scale rückgängig. Funktioniert.
            child.layoutY = //koordsys.kyToHöhe(imageKoordsys.kyMax)// *child.scaleY
                    //koordsys.Vh - imageKoordsys.Vh
                    + child.layoutBounds.height*(child.scaleY-1)/2
            println("New    "+ child.boundsInParent)

        }
    }
}