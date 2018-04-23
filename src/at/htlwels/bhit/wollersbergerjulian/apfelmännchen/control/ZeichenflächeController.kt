package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control.zeichnen.ZeichenStrategie
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control.zeichnen.ZeichenStrategienVerwalter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.EingabenController
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.RootLayoutController
import javafx.embed.swing.SwingFXUtils
import javafx.event.Event
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.image.WritableImage
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.stage.FileChooser
import javafx.stage.Window
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.naming.OperationNotSupportedException

// Created by julian on 27.08.17.
/**
 * Diese Klasse ist die Brücke (Schnittstelle) zwischen
 * GUI und Rechnen.
 *
 * Die GUI kann einerseits die Funktionen hier aufrufen,
 * um z.B. das Speichern auszulösen und andererseits
 * können die Berechnungen über [EingabenSchnittstelle]
 * auf die eingegebenen Werte zugreifen.
 *
 * Mauseingaben wie Zoomen und Bewegen werden von [MouseInputHandler] erledigt,
 * das auch hier verwaltet wird. (weil es Zugriff auf
 * das doppelKoordsys braucht.)
 *
 * RootLayout soll zeichenflächeRootPane zum SceneGraph
 * hinzufügen. Alle ZeichenRegionen sollten das GlobaleKoordsys verwenden.
 */
class ZeichenflächeController(
        eingabenController: EingabenController,
        private val rootLayoutController: RootLayoutController
) {
    /** Dieser Node soll zum SceneGraph hinzugefügt werden.
     * Derzeit ist es einfach das zeichenStackPane.
     * Dieser Name soll sich nicht ändern. */
    val zeichenflächeRootPane: Pane

    /** zeichenRegion ist im AnchorPane ist im StackPane.
     * Das StackPane ist für prefSize zuständig (denn es kann
     * im Gegensatz zum AnchorPane auch kleiner als diese sein). */
    private val zeichenStackPane: StackPane
    /** Das AnchorPane vergrößert und verkleinert die zeichenRegion
     * und enthält die EventListener. */
    private val zeichenAnchorPane: AnchorPane
    /** Ein Kleines Gimmik, das beim Laden gesetzt werden
     * soll. Schöner wärs In SpeichernRegion */
    private var ladeDingsbums: Node? = null

    private val zeichenStrategieVerwalter: ZeichenStrategienVerwalter

    /** Damit kann die Berechnung auf Eingaben zugreigen. */
    val eingaben = EingabenSchnittstelle(eingabenController)
    /** zoomen, bewegen; manipuliert globalesKoordsys. */
    private val mouseInputHandler: MouseInputHandler

    /** Das Koordinatensystem, das durch MouseInputHandler,... verändert wird. */
    var globalesKoordsys: DoppelKoordinatenSystem

    val zeichenflächePrefWidth = 600.0
    val zeichenflächePrefHeight = 450.0



    init {
        globalesKoordsys = DoppelKoordinatenSystem(
                eingaben.bereich,
                zeichenflächePrefWidth,
                zeichenflächePrefHeight)

        /* Das StackPane enthält das AnchorPane,
         * das die ZeichenStrategie-Pane enthält. */
        zeichenAnchorPane = AnchorPane()
        zeichenStackPane = StackPane(zeichenAnchorPane)
        zeichenStackPane.setPrefSize(zeichenflächePrefWidth, zeichenflächePrefHeight)

        zeichenStrategieVerwalter = ZeichenStrategienVerwalter(this, zeichenAnchorPane)
        zeichenStrategieVerwalter.setMultithreadedStrategie()

        // Angezeigt werden soll das StackPane mit Inhalten.
        zeichenflächeRootPane = zeichenStackPane

        mouseInputHandler = MouseInputHandler(this)
    }

    /** Das Koordsys in den Grundzustand versetzen. */
    fun resetZeichenRegion() {
        globalesKoordsys = DoppelKoordinatenSystem(
                eingaben.bereich,
                zeichenflächeRootPane.width,
                zeichenflächeRootPane.height
        ).entzerre()
        zeichenStrategieVerwalter.aktualisiere()
    }

    /** Stößt die Berechnung an. Für die EventHandler.
     * Oder um mit aktualisierten Eingaben zu arbeiten. */
    fun berechneBild() {
        zeichenStrategieVerwalter.aktualisiere()
    }

    /** Speichert ein Bild des aktuellen Bereiches,
     * aber mit der vom Nutzer eingegebenen Auflösung.
     *
     * Vor langer Zeit (Nov 2017) war ein Screenshot eine Option. */
    fun speichereZeichenfläche() {
        // Kleines Lade-Dingsbums :)
        setLadeDingsbums(Label("Bild wird berechnet..."))

        val beides = ZeichenStrategienVerwalter(this, zeichenAnchorPane)
        beides.setBildSpeichernStrategie(this)
        beides.aktualisiere() // Speichern

        setLadeDingsbums(null)
    }

    /** Zum registrieren von Events.
     *
     * Um sicherzugehen, dass alle Events nicht nur
     * auf ein einzelnes Kind angewendet werden,
     * werden sie von den Eltern behandelt. */
    fun <T : Event> addEventHandler(
            eventType: EventType<T>,
            eventHandler: EventHandler<T>) {
        zeichenAnchorPane.addEventHandler(eventType, eventHandler)
    }

    /** Das Window, das als Owner von Dialogen und dergleichen
     * verwendet werden soll. */
    fun getOwner(): Window {
        return rootLayoutController.getMainApp().getPrimaryStage()
    }

    /** Ein Ladedingsbums :)
     * Wenn null übergeben wird, wird es wieder gelöscht. */
    fun setLadeDingsbums(node: Node?) {
        if (ladeDingsbums != null)
            zeichenflächeRootPane.children.remove(ladeDingsbums)
        if (node != null)
            zeichenflächeRootPane.children.add(node)

        ladeDingsbums = node
    }
}
