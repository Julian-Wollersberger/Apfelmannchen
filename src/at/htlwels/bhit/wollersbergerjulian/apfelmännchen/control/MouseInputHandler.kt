package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.alleIterationen
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent

// Created by julian on 06.12.17.

/**
 * Diese Klasse verarbeitet die Maus-Aktionen, die
 * der Benutzer über der Zeichenfläche macht,
 * wie z.B. zoomen und drag&drop.
 *
 * Dabei wird das globaleKoordsys manipuliert
 * und die Berechnung angestoßen.
 */
class MouseInputHandler(
        private val controller: ZeichenflächeController
) {
    /** Registriert die EventHandler */
    init {
        // Scrollen
        controller.addEventHandler(ScrollEvent.SCROLL, EventHandler { zoomeBeimScrollen(it) })
        // Herumbewegen
        controller.addEventHandler(MouseEvent.MOUSE_PRESSED, EventHandler { startePressDragRelease(it) })
        controller.addEventHandler(MouseEvent.MOUSE_RELEASED, EventHandler { dragBeendet(it) })
        // Punkt des Mausklicks ausgeben
        controller.addEventHandler(MouseEvent.MOUSE_CLICKED, EventHandler {
            println("Geklickt auf r=${koordsys.breiteToKX(it.x)} i=${koordsys.höheToKY(it.y)}")
            println("Anzahl Iterationen:"+ alleIterationen(koordsys.breiteToKX(it.x), koordsys.höheToKY(it.y),
                    controller.eingaben.maxIterationen, controller.eingaben.maxDistanz).size)
        })

        // Wenn sich die Fenstergröße ändert
        controller.zeichenflächeRootPane.layoutBoundsProperty().addListener {
            observable, oldValue, newValue ->
            koordsys = DoppelKoordinatenSystem(
                    koordsys.kBereich,
                    newValue.width,
                    newValue.height
            ).entzerre()
            controller.berechneBild()
        }
    }

    /** Das Koordinatensystem, das durch den Benutzer verändert wird,
     * wird vom Controller verwaltet. */
    private var koordsys: DoppelKoordinatenSystem
        get() = controller.globalesKoordsys
        set(value) {controller.globalesKoordsys = value}

    private var dragStartEvent: MouseEvent? = null


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
        if (neuesKoordsys.kxSpanne != Double.NaN
                && neuesKoordsys.kySpanne != Double.NaN
                && neuesKoordsys.breite != Double.NaN
                && neuesKoordsys.höhe != Double.NaN
                ) {
            koordsys = neuesKoordsys
        } else
            println("Koordsys auf einmal NaN geworden! Zoomfaktor=$zoomFaktor \n" +
                    "Alt=$koordsys \nNeu=$neuesKoordsys")

        controller.berechneBild()
        event.consume()
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
            if (event.x != start.x || event.y != start.y)
                bewege(event.x - start.x, event.y - start.y)
        }
    }

    /** Das Bild bewegen.
     * @param x um wie viel in Breite-Richtung bewegen
     * @param y um wie viel in Höhe-Richtung bewegen */
    private fun bewege(x: Double, y: Double) {
        koordsys = koordsys.verschiebeUmPixel(x, y)
        controller.berechneBild()
    }
}
