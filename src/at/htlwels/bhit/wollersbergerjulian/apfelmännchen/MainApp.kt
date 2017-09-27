package at.htlwels.bhit.wollersbergerjulian.apfelmännchen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.HauptfensterController
import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import javafx.stage.WindowEvent

/**
 * <h1>Apfelmännchen</h1>
 * Das Apfelmännchen (oder Mandelbrotmenge, engl. Mandelbread Set)
 * ist ein mathematisches Fraktal mit einem sehr chaotischem Muster.
 *
 * <h1>Algorithmus</h1>
 * https://de.wikipedia.org/wiki/Mandelbrot-Menge<br>
 * Erklärung: siehe {@link at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.rechnen.Berechnung#istInMenge(double, double, int, double) istInMenge()}
 * <br><br>
 * z(0) = 0 <br>
 * z(n) = z(n+1)² + c <br>
 *
 * Wobei für jeden Punkt c der komplexen Zahlen-Ebene diese Folge berechnet wird.
 * Wenn die Folge zum Ursprung konvergiert, ist der Punkt in der Menge,
 * sonst ist er außerhalb. <br> <br>
 *
 * Die Farben repräsentieren die Anzahl der Iterationen, die berechnet werden,
 * bis feststeht, ob die Folge bei dem Punkt konvergiert.
 */
class MainApp : Application() {

    var primaryStage: Stage? = null
    var primaryScene: Scene? = null
    var hauptfensterController: HauptfensterController? = null

    override fun start(primaryStage: Stage?) = if (primaryStage == null)
        throw RuntimeException("Keine primaryStage!")
     else {
        this.primaryStage = primaryStage
        primaryStage.title = "Apfelmännchen"
        //primaryStage.isMaximized = true
        primaryStage.scene = initHauptfenster()
        primaryStage.show()

        (hauptfensterController as HauptfensterController).manuelleLayoutKorrekturen()
        //primaryStage.setOnShown {
            (hauptfensterController as HauptfensterController).resetZeichenregion(null)
        //}
    }

    fun initHauptfenster(): Scene? {
        // Lade root Layout von FXML Datei
        val loader = FXMLLoader()
        @Suppress("JAVA_CLASS_ON_COMPANION")
        loader.location = this.javaClass.getResource("view/Hauptfenster.fxml")
        val rootLayout: Any? = loader.load()

        // zeige Scene mit root Layout an
        val scene: Scene? = Scene(if (rootLayout != null && rootLayout is Parent) rootLayout else null )
        primaryScene = scene

        //Verknüpfe Controller mit Dieser Klasse
        val controller = loader.getController<HauptfensterController>()
        controller.setMainApp(this)
        hauptfensterController = controller

        return scene
    }
}

fun main(args: Array<String>) {
    Application.launch(MainApp::class.java, *args)
}

/*
* I have bugs in my Code and they won't go,
* glitches in my Code and they won't go.
*/