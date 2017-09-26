package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.anzeigen;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * https://de.wikipedia.org/wiki/Mandelbrot-Menge
 *
 * z(0) = 0
 * z(n) = z(n+1)² + c
 *
 * Wobei für jeden Punkt c der komplexen Zahlen-Ebene diese Folge berechnet wird.
 * Wenn die Folge zum Ursprung konvergiert, ist der Punkt in der Menge,
 * sonst ist er außerhalb.
 *
 * Die Farben repräsentieren die Anzahl der Iterationen, die berechnet werden,
 * bis feststeht, ob die Folge bei dem Punkt konvergiert.
 */
public class Main extends Application{

    private UserInterface userInterface = new UserInterface();

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Apfelmännchen");
        primaryStage.setScene(userInterface.erstelleScene());
        primaryStage.show();

    }

}

/*
* I have bugs in my Code and they won't go,
* glithches in my code head and they won't go.
* */