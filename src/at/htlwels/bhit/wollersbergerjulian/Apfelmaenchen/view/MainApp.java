package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.view;

import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.debug.DebugFensterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

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
public class MainApp extends Application{

    private Stage primaryStage;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        primaryStage.setTitle("Apfelmännchen");
        // Das Hauptfenster mit den Eingabefeldern und der Zeichnung.
        Scene scene = initZeichenfenster();
        primaryStage.setScene(scene);
        primaryStage.show();

        //TODO Diese Zahlen sind vom BorderPane. Irgendwie abfragen.
        //primaryStage.setMinHeight(primaryStage.getMinHeight() + 540);
        primaryStage.setMinWidth(primaryStage.getMinWidth() + 190);

        // Test für den Farbalgorithmus.
        //primaryStage.setScene(new Scene(new Pane(new FarbalgorithmusTest().teste())));

        Stage debugStage = new Stage();
        debugStage.setTitle("Apfelmännchen Debug");
        Scene debugScene = initDebugFenster(scene.getRoot());
        debugStage.setScene(debugScene);
        debugStage.show();

    }

    private Scene initZeichenfenster() {
        Scene scene = null;
        try {

            // Lade root Layout von FXML Datei
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("ZeichenFenster.fxml"));
            BorderPane rootLayout = loader.load();

            // zeige Scene mit root Layout an
            scene = new Scene(rootLayout);

            //Verknüpfe Controller mit Dieser Klasse
            ZeichenFensterController controller = loader.getController();
            controller.setMainApp(this);


            //rootLayout.prefWidthProperty().bind(scene.widthProperty());
            //rootLayout.prefHeightProperty().bind(scene.heightProperty());


        } catch (IOException e) {
            e.printStackTrace();
        }

        return scene;
    }

    private Scene initDebugFenster(Parent zeichenFensterRootLayout) {
        Scene scene = null;
        try {
            // Lade root Layout von FXML Datei
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DebugFensterController.class.getResource("DebugFenster.fxml"));
            BorderPane rootLayout = loader.load();

            // zeige Scene mit root Layout an
            scene = new Scene(rootLayout);

            //Verknüpfe Controller mit Dieser Klasse
            DebugFensterController controller = loader.getController();
            controller.setMainApp(this);
            controller.fülleGridPane(zeichenFensterRootLayout);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return scene;
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }
}

/*
* I have bugs in my Code and they won't go,
* glitches in my Code and they won't go.
*/