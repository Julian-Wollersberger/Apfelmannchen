package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.anzeigen;

import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.rechnen.CanvasAnimator;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Created by julian on 25.05.17.
 *
 * Diesie Klasse erstellt die Scene mit allen Eingabefeldern
 * und sie enthält die Methoden zur Eventbehandlung.
 */
public class UserInterface{

    private KomplexesCanvas canvas;
    private CanvasAnimator animator;

    private Label iterationenLabel = new Label("Iterationen: ");
    private Label threadsLabel = new Label("Threads: ");
    private Label minRLabel = new Label("Min R: ");
    private Label maxRLabel = new Label("Max R: ");
    private Label minILabel = new Label("Min I: ");
    private Label maxILabel = new Label("Max I: ");

    private TextField iterationenEingabe = new TextField("50");
    private TextField threadsEingabe = new TextField("8");
    private TextField minREingabe = new TextField("-2");
    private TextField maxREingabe = new TextField("1");
    private TextField minIEingabe = new TextField("-1.2");
    private TextField maxIEingabe = new TextField("1.2");

    private Button berechneButton = new Button("Berechne");

    private Pane canvasPane = new Pane();
    private GridPane eingaben = new GridPane();
    private VBox uiUndCanvas = new VBox(5, eingaben, berechneButton,canvasPane);
    private Scene scene = new Scene(uiUndCanvas);

    /**Diese Methode erstellt die Elemente des Fensters, also
     * Eingabefelder, Buttons und eine Gruppe, wo die Canvas
     * übereinander liegen.
     * @return Die Scene, die in die primaryStage kommen soll.*/
    public Scene erstelleScene()
    {
        eingaben.add(iterationenLabel, 0, 0);
        eingaben.add(iterationenEingabe, 1, 0);
        eingaben.add(threadsLabel, 0, 1);
        eingaben.add(threadsEingabe, 1, 1);

        eingaben.add(minRLabel, 2, 0);
        eingaben.add(maxRLabel, 2, 1);
        eingaben.add(minREingabe, 3, 0);
        eingaben.add(maxREingabe, 3, 1);
        eingaben.add(minILabel, 4, 0);
        eingaben.add(maxILabel, 4, 1);
        eingaben.add(minIEingabe, 5, 0);
        eingaben.add(maxIEingabe, 5, 1);

        canvasPane.setPrefWidth(1000);
        canvasPane.setPrefHeight(800);

        berechneButton.setOnAction(e -> {macheNeuesApfelmännchen(e);});
        //Events für canvas werden beim erstellen gesetzt. Unschön.

        return scene;
    }

    public void macheNeuesApfelmännchen(Event event)
    {
        //System.out.println("macheNeuesApfelmännchen()");
        try {
            if(animator!=null)
                animator.stop();
            if(canvasPane!=null)
            {
                // Zerstören
                canvasPane.getChildren().remove(canvas);

                //Das Ding, auf dem gezeichnet wird neu machen
                canvas = new KomplexesCanvas(canvasPane.getWidth(),
                        Double.parseDouble(minREingabe.getText()),
                        Double.parseDouble(maxREingabe.getText()),
                        Double.parseDouble(minIEingabe.getText()),
                        Double.parseDouble(maxIEingabe.getText()),
                        Integer.parseInt(iterationenEingabe.getText()),
                        Color.WHITE);
                canvasPane.getChildren().add(canvas);

                System.out.println(canvas);
                canvasPane.setPrefHeight(canvasPane.getWidth() * canvas.berechneSeitenverhältnis());
                canvas.setOnZoom(e -> {
                    zoomeBeimWischen(e);});
                canvas.setOnScroll(event1 -> zoomeBeimScrollen(event1));

                //Die Berechnung starten
                animator = new CanvasAnimator(canvas, Integer.parseInt(threadsEingabe.getText()));
                animator.start();
            }
        }
        catch (NumberFormatException e) {
            showDialog(scene.getWindow(), "Zahlen ungültig");
            System.out.println("Ungültiger Wert "+ iterationenEingabe.getText());
            System.out.println("Ungültiger Wert "+ threadsEingabe.getText());
        }
    }

    /** Das Canvas wird skalliert und so verschoben, das der Zentrum-Punkt da bleibt, wo er war. */
    //TODO zentrum
    public void zoome(double faktor, double zentrumX, double zentrumY)
    {
        canvas.setScaleX(faktor);
        canvas.setScaleY(faktor);
    }

    /** Für ein Finger-Wisch-Zoomen
     * TODO Wischen testen */
    private void zoomeBeimWischen(ZoomEvent event)
    {
        if(event.equals(ZoomEvent.ZOOM))
            canvas.setScaleX(event.getZoomFactor());
            canvas.setScaleY(event.getZoomFactor());
    }

    /** Wenn das */
    private void zoomeBeimScrollen(ScrollEvent event)
    {
        double delta = event.getDeltaX() + event.getDeltaY();

        zoome(delta/10, 0,0);
    }

    public static void showDialog(Window owner, String text) {
        Label textLabel = new Label(text);
        Scene scene = new Scene(textLabel);
        // Create a Stage with specified owner and modality
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.show();
    }
}
