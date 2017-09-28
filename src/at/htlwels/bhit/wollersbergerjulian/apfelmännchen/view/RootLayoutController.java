package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// Created by julian on 25.08.17.
/**
 * Im RootLayout ist die Menüleiste.
 * Diese soll auch in eine eigene Klasse, wenn
 * sie irgendwann mal verwendet wird.
 *
 * Die Zeichenfläche und die Eingaben werden
 * jeweils mit Controllern geladen und initialisiert.
 *
 * Die jeweiligen Controller übernehmen die Funktionalitäten,
 * insbesondere der {@link ZeichenflächeController} für
 * das Zeichnen.
 *
 * TODO Reset-Button oder Menü-Eintrag
 */
public class RootLayoutController implements Initializable {

    @FXML private BorderPane rootLayout;

    private EingabenController eingabenController;
    private ZeichenflächeController zeichenflächeController;
    private MenüLeisteController menüLeisteController;
    //Menü-Leiste-Controller

    private MainApp mainApp;

    /** Die Zeichenfläche und die Eingaben werden
     * jeweils mit Controllern geladen und
     * die Nodes dem rootLayout hinzugefügt. */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootLayout.setLeft(initEingaben());
        rootLayout.setTop(initMenüLeiste());

        zeichenflächeController = new ZeichenflächeController(eingabenController, this);
        Node zeichenfläche = zeichenflächeController.getZeichenflächeRootPane();
        rootLayout.setCenter(zeichenfläche);
    }

    /* Lädt Eingaben.fxml und gibt den geladenen
     * Node zurück. */
    private Node initEingaben() {
        Node eingaben = null;
        try {
            // Lade Layout von FXML Datei
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("Eingaben.fxml"));
            eingaben = loader.load();

            //Der heilige Controller!
            eingabenController = loader.getController();
            eingabenController.setRootLayoutController(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return eingaben;
    }

    /* Lädt MenüLeiste.fxml und gibt den geladenen
     * Node zurück. */
    private Node initMenüLeiste() {
        Node menüLeiste = null;
        try {
            // Lade Layout von FXML Datei
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("MenüLeiste.fxml"));
            menüLeiste = loader.load();

            //Der heilige Controller!
            menüLeisteController = loader.getController();
            menüLeisteController.setRootLayoutController(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return menüLeiste;
    }

    /** Es gibt Dinge, die erst aufgerufen werden sollen, nachdem
     * primaryStage.show() aufgerufen wurde, weil dannach erst die
     * größen berechnet sind. */
    void nachStageShow() {
        zeichenflächeController.resetZeichenRegion();
    }

    MainApp getMainApp() {
        return mainApp;
    }
    void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    ZeichenflächeController getZeichenflächeController() {
        return zeichenflächeController;
    }
}
