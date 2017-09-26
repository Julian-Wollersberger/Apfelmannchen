package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.anzeigen;

import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.rechnen.Berechnung;
import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.rechnen.PunktZeiger;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

/**
 * Created by julian on 25.05.17.
 */
public class TestAbschnitte {

    public void start(Stage primaryStage) {

        int größe = 700;

        Berechnung berechnung = new Berechnung(-2, 1.2, -1, 1.2, 15);
        //Berechnung berechnung = new Berechnung(-1, -0.5, -0.2, 0.2, 100);
        //Berechnung berechnung = new Berechnung(-0.8, -0.7, -0.02, 0.02, 1000);
        //Berechnung berechnung = new Berechnung(-0.5, 0.5, -0.5, 0.5, 100);


        Canvas menge = new Canvas(größe, berechnung.berechneHöhe(größe));
        Canvas einzelPunkte = new Canvas(größe, berechnung.berechneHöhe(größe));
        einzelPunkte.setOpacity(0.7);
        Group root = new Group(menge/*, einzelPunkte*/);

        PunktZeiger punktZeiger = new PunktZeiger(-2, 1, -1, 1, menge);


        //Scene scene = new Scene(root, größe, berechnung.berechneHöhe(größe));
        //menge.setHeight();


        primaryStage.setTitle("Apfelmännchen");
        //primaryStage.setScene(UserInterface.erstelleScene(root));
        primaryStage.show();

        // Und jetzt alles berechnen
        berechnung.zeichne(menge);
        /*punktZeiger.zeichne(-0.5,0.5);
        punktZeiger.zeichne(-0.5,-0.5);
        punktZeiger.zeichne(-0.5,0);
        punktZeiger.zeichne(0.5,0);
        punktZeiger.zeichne(-1,0);
        punktZeiger.zeichne(-1.5,0);
        punktZeiger.zeichne(-1,0.2);*/

    }
}
