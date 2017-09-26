package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.rechnen;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by julian on 21.04.17.
 */
@Deprecated
public class PunktZeiger {

    private Canvas canvas;
    // In welchem Bereich die c-Punkte liegen
    private double minR;
    private double maxR;
    private double minI;
    private double maxI;

    public PunktZeiger(double minR, double maxR, double minI, double maxI, Canvas canvas) {
        this.minR = minR;
        this.maxR = maxR;
        this.minI = minI;
        this.maxI = maxI;

        this.canvas = canvas;
    }
    /*public PunktZeiger() {
        this.minR = -2;
        this.maxR = 1;
        this.minI = -1;
        this.maxI = 1;
    }*/

    public void zeichne(double cr, double ci)
    {
        // Canvas vorbereiten
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        //gc.clearRect(0,0,canvas.getWidth(), canvas.getHeight());

        //Die Größe in Pixel
        double breite = canvas.getBoundsInLocal().getWidth();
        double höhe = canvas.getBoundsInLocal().getHeight();

        // Wie viel ein Pixel entspricht.
        double pixelR = (maxR - minR) / breite;
        double pixelI = (maxI - minI) / höhe;

        // Die fortlaufenden Punkte

        System.out.println(breite +" "+ höhe);
        System.out.println(pixelR +" "+ pixelI);

        double zr = cr;
        double zi = ci;
        double zrtemp;

        gc.setStroke(new Color(0, 1, 0,1));

        // Die Haupt-Schleife
        for (int i = 0; i < 100 && zr+zi <2 ; i++) {
            zrtemp =  zr*zr - zi*zi +cr;
            zi = 2*zr*zi + ci;
            zr = zrtemp;

            gc.strokeLine((zr-minR)/pixelR, (zi-minI)/pixelI, (zr-minR)/pixelR, (zi-minI)/pixelI);
            System.out.println((zr-minR)/pixelR +" \t"+ (zi-minI)/pixelI);
        }

    }
}
