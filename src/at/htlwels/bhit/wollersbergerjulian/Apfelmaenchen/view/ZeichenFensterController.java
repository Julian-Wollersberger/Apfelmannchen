package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.view;

import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.anzeigen.KomplexesCanvas;
import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.anzeigen.ZoomController;
import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.model.Bereich;
import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.rechnen.CanvasAnimator;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;

// Created by julian on 03.07.17.
/**
 * <h1>Zeichenfenster</h1>
 * Das Zeichenfenster ist das Haupt-Fenster der ganzen Anwendung.
 * Auf diesem Fenster sind das Apfelmännchen ({@link KomplexesCanvas})
 * und die Eingaben der Werte.<br>
 * Gemacht im SceneBuilder. <br><br>
 *
 * Besonderheit des Zeichenflächen-Layouts:<br>
 * Es enthält ein leeres AnchorPane ohne Kinder und das Canvas
 * ist auf der selben Hierarchiestufe.<br>
 * Grund: Das AnchorPane unterschreitet die minimale Größe seiner Kinder
 * nicht und das Canvas ist so groß wie es Pixel hat. Wenn nun das Canvas Kind
 * von AnchorPane wäre, und man das Fenster größer und später wieder kleiner macht,
 * bleibt das Pane groß. Dadurch wird der Berechnen-Button abgeschnitten.<br>
 * Deshalb überwacht das Canvas nur die Größe des leeren AnchorPanes und
 * passt sich damit an.
 */
public class ZeichenFensterController implements Initializable {

    private KomplexesCanvas canvas;
    private CanvasAnimator animator;

    private MainApp mainApp;

    @FXML private TextField iterationenEingabe;
    @FXML private TextField threadsEingabe;
    @FXML private TextField minREingabe;
    @FXML private TextField maxREingabe;
    @FXML private TextField minIEingabe;
    @FXML private TextField maxIEingabe;

    @FXML private GridPane eingabeGridPane;
    @FXML private StackPane stackPane;
    @FXML private Button berechneButton;
    @FXML private AnchorPane canvasPane;
    @FXML private Pane canvasGroup;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        iterationenEingabe.setText(Integer.toString(Standardwerte.MAX_ITERATIONEN));
        threadsEingabe.setText(Integer.toString(Standardwerte.ANZAHL_THREADS));
        minREingabe.setText(Double.toString(Standardwerte.MIN_R));
        maxREingabe.setText(Double.toString(Standardwerte.MAX_R));
        minIEingabe.setText(Double.toString(Standardwerte.MIN_I));
        maxIEingabe.setText(Double.toString(Standardwerte.MAX_I));

        AnchorPane.setLeftAnchor(stackPane, eingabeGridPane.getLayoutBounds().getMaxY());

        canvasPane.setStyle("-fx-border-style: solid; -fx-border-width: 2px");

        canvas = new KomplexesCanvas(0.0, new Bereich(0,0,0,0), 0, null);
        canvasGroup.getChildren().add(canvas);

        // Damit das Canvas sich vergrößert.
        canvasPane.boundsInParentProperty().addListener(
                (observable, oldValue, newValue) -> canvas.resizeRelocate(
                        newValue.getMinX(), newValue.getMinY(), newValue.getWidth(), newValue.getHeight()));
    }


    //TODO Dieser Code ist hier nicht sehr schön.
    public void macheNeuesApfelmännchen(Event event)
    {
        //System.out.println("macheNeuesApfelmännchen()");
        try {
            if(animator!=null)
                animator.stop();
            if(canvasGroup!=null)
            {
                // Zerstören
                canvasGroup.getChildren().remove(canvas);

                Bereich bereich = new Bereich(
                        Double.parseDouble(minREingabe.getText()),
                        Double.parseDouble(maxREingabe.getText()),
                        Double.parseDouble(minIEingabe.getText()),
                        Double.parseDouble(maxIEingabe.getText()));
                //Das Ding, auf dem gezeichnet wird neu machen
                canvas = new KomplexesCanvas(canvasPane.getWidth(),
                        bereich,
                        Integer.parseInt(iterationenEingabe.getText()),
                        Standardwerte.GRUNDFARBE);

                canvasGroup.getChildren().add(canvas);

                ZoomController zoomController = new ZoomController(canvas);

                System.out.println(canvas);
                //TODO Höhe setzen funktioniert nicht.
                canvasPane.setPrefHeight(canvasPane.getWidth() * canvas.berechneSeitenverhältnis());
                System.out.println(canvasPane.getWidth() * canvas.berechneSeitenverhältnis());
                canvasPane.resize(canvasPane.getWidth(), canvasPane.getPrefHeight());

                canvas.setOnZoom(zoomController::zoomeBeimWischen);
                canvas.setOnScroll(zoomController::zoomeBeimScrollen);
                canvas.setOnMouseDragged(zoomController::bewege);
                canvas.setOnMouseReleased(zoomController::endeBewegung);

                //Die Berechnung starten
                animator = new CanvasAnimator(canvas, Integer.parseInt(threadsEingabe.getText()));
                animator.start();
            }
        }
        catch (NumberFormatException e) {
            showDialog(mainApp.getPrimaryStage(), "Zahlen ungültig");
            System.out.println("Ungültiger Wert "+ iterationenEingabe.getText());
            System.out.println("Ungültiger Wert "+ threadsEingabe.getText());
        }
    }

    private void showDialog(Window owner, String text) {
        Label textLabel = new Label(text);
        Scene scene = new Scene(textLabel);

        // Create a Stage with specified owner and modality
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.show();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
