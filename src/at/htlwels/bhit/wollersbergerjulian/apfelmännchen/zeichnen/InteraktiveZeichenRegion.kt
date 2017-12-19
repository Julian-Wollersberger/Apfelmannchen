package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.zeichnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.*
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control.ZeichenflächeController
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent

// Created by julian on 01.09.17.
/**
 * Dies ist eine ZeichenRegion, die auf Mauseingaben
 * reagiert mittels MouseEvents.
 *
 * Gezoomt wird durch Scrollen und das Bild
 * bewegt mit Press-Drag-Release.
 * Dabei ist ein [Verzögerer] eingebaut, damit der
 * Benutzer eine Geste vollständig machen kann, ohne
 * dass bei jedem Teilschritt berechnet wird.
 *
 * Die Eingaben verändern das globaleKoordsys und das
 * Bild wird damit und mit aktualisierten Eingaben
 * neu berechnet.
 *
 * Was leider nicht geht, ist die Eingaben
 * flüssig zu gestalten, mit scale() und translate().
 */
class InteraktiveZeichenRegion(
        private val controller: ZeichenflächeController
) : ZeichenRegion() {
    private val eingaben = controller.eingaben

    /** Das Koordinatensystem, das durch den Benutzer verändert wird,
     * wird vom Controller verwaltet. */
    private var koordsys: DoppelKoordinatenSystem
        get() = controller.globalesKoordsys
        set(value) {controller.globalesKoordsys = value}

    private var imageView = ImageView()
    private val verzögerer = Verzögerer()
    private var animi: BildAnimierer? = null
    private var dragStartEvent: MouseEvent? = null


    //TODO Weg da!
    override fun registerEventHanders() {
        // Scrollen
        controller.addEventHandler(ScrollEvent.SCROLL, EventHandler { zoomeBeimScrollen(it) })
        // Herumbewegen
        controller.addEventHandler(MouseEvent.MOUSE_PRESSED, EventHandler { startePressDragRelease(it) })
        controller.addEventHandler(MouseEvent.MOUSE_RELEASED, EventHandler { dragBeendet(it) })
        // Punkt des Mausklicks ausgeben
        controller.addEventHandler(MouseEvent.MOUSE_CLICKED, EventHandler {
            println("Geklickt auf r=${koordsys.breiteToKX(it.x)} i=${koordsys.höheToKY(it.y)}")
            println("Anzahl Iterationen:"+ alleIterationen(koordsys.breiteToKX(it.x), koordsys.höheToKY(it.y),
                    eingaben.eingabeMaxIterationen, eingaben.eingabeMaxDistanz).size)
        })
    }

    /**Diese Methode berechnet ein neues Bild mit den
     * Eingaben im Hauptfenster. */
    override fun reset() {
        /* Mit den aktuellen Werten ein neues entzerrtes Koordsys berechnen. */
        koordsys = DoppelKoordinatenSystem(
                eingaben.eingabeBereich,
                getContentWidth(),
                getContentHeight()
        ).entzerre()
        berechneBildVerzögert(0L)
    }

    override fun berechneBild() {
        berechneBildVerzögert(Verzögerer.standardVerzögerung)
    }

    /**Beauftragt einen neuen Thread, der
     * das Bild berechnet. Der Delay ist
     * für Eingaben gedacht, damit nicht jedes
     * einzelne der Vielen Auslösungen des
     * Events zum neu berechnen führt. */
    fun berechneBildVerzögert(delay: Long) {
        /* Mit den aktuellen Werten ein neues entzerrtes Koordsys berechnen. */
        koordsys = DoppelKoordinatenSystem(
                koordsys.kBereich,
                getContentWidth(),
                getContentHeight()
        ).entzerre()
        val neuesImageKoordsys = koordsys.copy()
        println("${neuesImageKoordsys.breite.toInt()} ${neuesImageKoordsys.höhe.toInt()} $neuesImageKoordsys")
        val parameter = eingaben.eingabeParameter


        //TODO Beschreibung
        verzögerer.berechneVerzögert(delay, Runnable{
        //GlobalerThreadManager.berechne(Runnable {
        val alterAnimi = animi
        if(alterAnimi != null)
            alterAnimi.stop()

        val neuerAnimator = berechneAnimiert(koordsys, parameter)
        animi=neuerAnimator
        println("Es berechnet jetzt animiert!")

        Platform.runLater {
        val view = ImageView(neuerAnimator.writableImage)
        imageView = view

        children.clear()
        children.add(view)
            }
        })
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
    private fun zoomeBeimScrollen(event: ScrollEvent) {
        val zoomFaktor = 1 - event.deltaY / 120

        val neuesKoordsys = koordsys.erzeugeKartesischenAusschnitt(zoomFaktor, event.x, event.y)
        if(neuesKoordsys.kxSpanne != Double.NaN
                && neuesKoordsys.kySpanne != Double.NaN
                && neuesKoordsys.breite != Double.NaN
                && neuesKoordsys.höhe != Double.NaN
                ) {
            koordsys = neuesKoordsys
        } else
            println("Koordsys auf einmal NaN geworden! Zoomfaktor=$zoomFaktor \n" +
                    "Alt=$koordsys \nNeu=$neuesKoordsys")

        // Das ruft [layoutChildren] auf, damit das ImageView angepasst werden kann.
        requestLayout()
        berechneBildVerzögert(Verzögerer.standardVerzögerung)
    }

    /** Wenn der Benutzer die Maus niederdrückt. */
    private fun startePressDragRelease(event: MouseEvent) {
        dragStartEvent = event
    }
    /** Beim Loslassen des Maus-Knopfes.
     * Ist sie seit dem Niederdrücken bewegt worden, so
     * verschiebe das Bidl. */
    private fun dragBeendet(event: MouseEvent) {
        // Wenn es nicht die selbe Position ist, dann bewege
        val start = dragStartEvent
        if(start != null) {
            if (event.x != start.x && event.y != start.y)
                bewege(event.x - start.x, event.y - start.y)
        }
    }

    /** Das Bild bewegen.
     * @param x um wie viel in Breite-Richtung bewegen
     * @param y um wie viel in Höhe-Richtung bewegen */
    private fun bewege(x: Double, y: Double) {
        koordsys = koordsys.verschiebeUmPixel(x, y)

        requestLayout()
        berechneBildVerzögert(0)
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