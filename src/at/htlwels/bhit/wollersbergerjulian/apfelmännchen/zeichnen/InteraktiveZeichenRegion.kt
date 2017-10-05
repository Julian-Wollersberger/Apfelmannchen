package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.zeichnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.ThreadManager
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.berechneBereich
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.berechneBereichMultiThreaded
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.ZeichenflächeController
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.input.DragEvent
import javafx.scene.input.ScrollEvent

// Created by julian on 01.09.17.
/**
 * Zeichnet und kann zoomen.
 *
 * TODO Beschreibung
 *
 * TODO Nächster größerer Schritt:
 * Hier herein soll wirklich nur noch das Layout kommen.
 * Eine Funktion berechneBild() : Image soll wo anders dazu kommen.
 *
 * Eine neue Klasse machen dafür:
 * Sie nimmt Bereich, breite und höhe und enthält eine
 * Funktionen, die ein Bild liefert, und eine, die Platform.runLater macht.
 *
 * Hier wird nur noch diese Klasse aufgerufen.
 *
 * Eigene Klasse für Event-Behandlung und resultierenden
 * manipulieren des globalenKoordinatensystem. EventController
 */
class InteraktiveZeichenRegion(
        private val controller: ZeichenflächeController
) : ZeichenRegion() {

    /** Das Koordinatensystem, das durch den Benutzer verändert wird,
     * wird vom Controller verwaltet. */
    private var koordsys: DoppelKoordinatenSystem
        get() = controller.globalesKoordsys
        set(value) {controller.globalesKoordsys = value}

    private var image = WritableImage(1, 1)
    private var imageView = ImageView()
    /** Das Koordsys, mit dem das aktuell in [imageView] gespeicherte
     * Bild berechnet wurde. */
    private var imageKoordsys = koordsys

    private val threadManager = ThreadManager()


    override fun registerEventHanders() {
        controller.addEventHandler(ScrollEvent.SCROLL, EventHandler { zoomeBeimScrollen(it) })
    }

    /**Diese Methode berechnet ein neues Bild mit den
     * Eingaben im Hauptfenster. */
    override fun reset() {
        /* Mit den aktuellen Werten ein neues entzerrtes Koordsys berechnen. */
        koordsys = DoppelKoordinatenSystem(
                controller.eingabeBereich,
                getContentWidth(),
                getContentHeight()
        ).entzerre()
        berechneBild(0L)
    }

    /**Beauftragt einen neuen Thread, der
     * das Bild berechnet. Der Delay ist
     * für Eingaben gedacht, damit nicht jedes
     * einzelne der Vielen Auslösungen des
     * Events zum neu berechnen führt. */
    fun berechneBild(delay: Long) {
        /* Mit den aktuellen Werten ein neues entzerrtes Koordsys berechnen. */
        koordsys = DoppelKoordinatenSystem(
                koordsys.kBereich,
                getContentWidth(),
                getContentHeight()
        ).entzerre()
        val neuesImageKoordsys = koordsys.copy()
        println("${neuesImageKoordsys.breite.toInt()} ${neuesImageKoordsys.höhe.toInt()} $neuesImageKoordsys")

        val parameter = controller.eingabeParameter
        val neuesImage = WritableImage(neuesImageKoordsys.breite.toInt(), neuesImageKoordsys.höhe.toInt())
        val pixelWriter = neuesImage.pixelWriter

        val berechnung = Runnable {
            berechneBereichMultiThreaded(
                    4, //TODO Eingabe!
                    neuesImageKoordsys,
                    parameter,
                    pixelWriter::setArgb
            )

            /** Macht ein neues ImageView aus dem
             * übergebenem Image. imageKoordsys wird aktualisiert.
             *
             * Es darf von einem beliebigen Thread aus
             * aufgerufen werden, weil es [Platform.runLater] aufruft. */
            Platform.runLater {
                image = neuesImage
                this.imageKoordsys = neuesImageKoordsys
                val view = ImageView(neuesImage)
                imageView = view

                children.clear()
                children.add(view)
            }
        }

        if (delay == 0L)
            ThreadManager.berechne(berechnung)
        else
            threadManager.berechneVerzögert(delay, berechnung)

    }

    private fun bewegeBeiDrag(event: DragEvent) {
        event.x
        event.y
    }

    /**Beim Zoomen soll der kartesische Punkt, über dem die Maus ist,
     * vorher und nachher gleich bleiben.
     *
     * Runterscrollen -> hineinzoomen
     *
     * Nach dem Zoomen sind der Bereich, V und scale
     * verändert; sie repräsentieren die neue Ansicht.
     * Das berechnete Bild passt nicht mehr dazu.
     *
     * //fixme koordsys wurde auf einmal NaN. Vielleicht war event.deltaY==0
     */
    private fun zoomeBeimScrollen(event: ScrollEvent) {
        val zoomFaktor = 1 - event.deltaY / 120

        koordsys = koordsys.erzeugeKartesischenAusschnitt(zoomFaktor, event.x, event.y)

        // Das ruft [layoutChildren] auf, damit das ImageView angepasst werden kann.
        requestLayout()

        berechneBild(500L)
    }

    /** Kind soll so groß sein wie Elter.
     * ImageView ist allerdings nicht resizeable,
     * deshalb muss es gezoomt werden.
     *
     * TODO Das Zoomen beim Layouten will einfach nicht funktionieren :(
     * Ich schaffe es nicht, eine Formel zu machen... */
    override fun layoutChildren() {
        // Child sollte das ImageView sein.
        for (child in children) {
            /* Das hier beeinflusst die LayoutBounds.
            * Die Verschiebung weiter unten wird dannach berechnet. */
            child.resizeRelocate(layoutX + insets.top, layoutY + insets.left,
                    getContentWidth(), getContentHeight())
        }

        // Für Versuche, wie das Zoomen funktionieren könnte, in V3 nachsehen.
    }
}