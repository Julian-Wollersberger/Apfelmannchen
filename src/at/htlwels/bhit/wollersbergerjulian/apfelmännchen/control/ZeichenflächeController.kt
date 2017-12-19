package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.Bereich
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.EingabenController
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.RootLayoutController
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.StandardwerteEingabe
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.zeichnen.InteraktiveZeichenRegion
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.zeichnen.SpeichernZeichenRegion
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.zeichnen.ZeichenRegion
import javafx.event.Event
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.scene.Node
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.stage.Window

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
 *
 * TODO Eigene Klasse für Events und in InteraktiveZeichenRegion nur das Bild berechnen?
 *
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
    private val zeichenRegion: ZeichenRegion
    /** Ein Kleines Gimmik, das beim Laden gesetzt werden
     * soll. Schöner wärs In SpeichernRegion */
    private var ladeDingsbums: Node? = null

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
                eingaben.eingabeBereich,
                zeichenflächePrefWidth,
                zeichenflächePrefHeight)

        // Heilige ZeichenRegion!
        zeichenRegion = InteraktiveZeichenRegion(this)

        /* Die ZeichenRegion soll sich mitvergrößern. */
        //zeichenRegion.style = "-fx-border-style: solid;-fx-border-width: 2px"
        AnchorPane.setLeftAnchor(zeichenRegion, 0.0)
        AnchorPane.setRightAnchor(zeichenRegion, 0.0)
        AnchorPane.setTopAnchor(zeichenRegion, 0.0)
        AnchorPane.setBottomAnchor(zeichenRegion, 0.0)

        /* Das StackPane enthält das AnchorPane,
         * das die ZeichenRegion enthält. */
        zeichenAnchorPane = AnchorPane(zeichenRegion)
        zeichenStackPane = StackPane(zeichenAnchorPane)
        zeichenStackPane.setPrefSize(zeichenflächePrefWidth, zeichenflächePrefHeight)

        mouseInputHandler = MouseInputHandler(this)
        //zeichenRegion.registerEventHanders()

        // Angezeigt werden soll das StackPane mit Inhalten.
        zeichenflächeRootPane = zeichenStackPane
    }

    /** Darf erst aufgerufen werden, nachdem primaryStage.show()
     * aufgeruen wurde. */
    fun resetZeichenRegion() {
        zeichenRegion.reset()
    }

    /** Stößt die Berechnung an. Für die EventHandler.
     * Oder um mit aktualisierten Eingaben zu arbeiten. */
    fun berechneBild() {
        zeichenRegion.berechneBild()
    }

    /** Speichert ein Bild des aktuellen Bereiches. */
    fun speichereZeichenfläche() {
        SpeichernZeichenRegion(this).berechneBildUndSpeichere()
        // Vor langer Zeit (Nov 2017) war ein Screenshot eine Option.

    }

    /** Zum registrieren von Events.
     *
     * Die ZeichenRegion kann Events nicht auf sich selbst
     * registrieren, denn wenn es zwei gibt, (also z.B.
     * InteraktiveZeichenRegion und PunktZeichenRegion),
     * dann bekommt nur einer die Events.
     * Wenn es aber auf ein Elter registriert wird, dann
     * bekommen das Event alle. */
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
    public fun setLadeDingsbums(node: Node?) {
        if (ladeDingsbums != null)
            zeichenflächeRootPane.children.remove(ladeDingsbums)
        if (node != null)
            zeichenflächeRootPane.children.add(node)

        ladeDingsbums = node
    }
}
