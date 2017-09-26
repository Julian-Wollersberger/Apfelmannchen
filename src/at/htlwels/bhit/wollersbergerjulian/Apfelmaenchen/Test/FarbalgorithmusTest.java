package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.Test;

import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.anzeigen.KomplexesCanvas;
import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.model.Bereich;
import javafx.scene.paint.Color;

/**
 * Created by julian on 01.07.17.
 *
 * Hier wird der Algorithmus zur Bereechnung der Farbe in KomplexesCanvas getestet.
 */
public class FarbalgorithmusTest {

    public FarbalgorithmusTest() {
    }

    public KomplexesCanvas teste() {
        int maxIterationen = 100;
        int iteration = 0;

        //TODO Test mit iterationen = INT_MAX

        KomplexesCanvas canvas = new KomplexesCanvas(200, new Bereich(-100, 100, -100, 100), maxIterationen, Color.BLUE);

        //i und j entsprechen hier cr und ci.
        for (int i = -100; i < 100; i++) {
            for (int j = -100; j < 100; j++) {

                canvas.zeichnePunkt(i, j, iteration);
                iteration++;
            }
        }

        return canvas;
    }


}
