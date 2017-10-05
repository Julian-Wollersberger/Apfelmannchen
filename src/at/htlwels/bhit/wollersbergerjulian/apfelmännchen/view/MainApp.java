package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * <h1>Apfelmännchen</h1>
 * Das Apfelmännchen (oder Mandelbrotmenge, engl. Mandelbread Set)
 * ist ein mathematisches Fraktal mit einem sehr chaotischem Muster.
 *
 * <h1>Algorithmus</h1>
 * https://de.wikipedia.org/wiki/Mandelbrot-Menge<br>
 * Erklärung: siehe {@link at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.BerechnungApfelmännchenKt#istInMenge(double, double, int, double) istInMenge()}
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
 *
 * Diese Anwendung sollte so konzipiert sein, dass man die
 * Apfelmännchen-Berechnung durch eine andere ersetzen kann.
 * Deshalb sind nur die wenigsten Komponenten wirklich nach
 * der Mandelbrotmenge benannt. z.B. cr,ci <-> kx, ky
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private RootLayoutController rootLayoutController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        // Die meiste Zauberei :)
        initRootLayout();

        // Stagelein zeig dich!
        primaryStage.show();

        rootLayoutController.nachStageShow();
    }


    /** Die meiste Zauberei.
     * Das ganze GUI wird in {@link RootLayoutController#initialize}
     * geladen.
     * @throws IOException Lade-Fehler werden nicht behandelt. */
    private void initRootLayout() throws IOException {
        try {
            // Lade root Layout von FXML Datei
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("RootLayout.fxml"));
            BorderPane rootLayout = loader.load();

            // zeige Scene mit root Layout an
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
            primaryStage.setTitle("Apfelmännchen");

            //Der heilige Controller!
            /* Es wird außerdem aufgerufen:
            {@link RootLayoutController#initialize}*/
            rootLayoutController = loader.getController();
            rootLayoutController.setMainApp(this);

        } catch (IOException e) {
            //e.printStackTrace();
            e.fillInStackTrace();
            throw e;
        }
    }

    /** Kann als Owner von anderen Fenstern
     * verwendet werden. */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}

/*
* I have bugs in my Code and they won't go,
* glitches in my Code and they won't go.
*/