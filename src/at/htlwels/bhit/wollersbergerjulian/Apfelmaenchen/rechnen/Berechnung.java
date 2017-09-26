package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.rechnen;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by julian on 30.03.17.
 */
@Deprecated
public class Berechnung {

    private int maxIterationen;

    // In welchem Bereich die c-Punkte liegen
    private double minR;
    private double maxR;
    private double minI;
    private double maxI;

    public Berechnung(double minR, double maxR, double minI, double maxI, int maxIterationen) {
        this.minR = minR;
        this.maxR = maxR;
        this.minI = minI;
        this.maxI = maxI;
        this.maxIterationen = maxIterationen;
    }
    public Berechnung() {
        this.minR = -2;
        this.maxR = 1;
        this.minI = -1;
        this.maxI = 1;
        this.maxIterationen = 100;
    }


    /** Testet solange, bis feststeht, ob es in oder außerhalb der Menge ist.
     *
     * Abbruchbedingungen:
     * 1000 Iterationen oder
     * zr + zi > 2 (Mehr als 2 vom Ursprung weg in Diamantform)
     *
     * @return Die Anzahl Iterationen, die er gebraucht hat. */
    public int istInMenge(double cr, double ci)
    {
        /* z(1) */
        double zr = cr;
        double zi = ci;
        double zrtemp;
        int i;

        for (i = 0; i < maxIterationen && zr+zi <2; i++) {
            zrtemp =  zr*zr - zi*zi +cr;
            zi = 2*zr*zi + ci;
            zr = zrtemp;
        }

        return i;
    }

    public double berechneHöhe(int breite)
    {
        return breite * (maxI - minI)/(maxR - minR);
    }

    public void zeichne(Canvas canvas)
    {
        // Canvas vorbereiten
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0,canvas.getWidth(), canvas.getHeight());

        //Die Größe in Pixel
        double breite = canvas.getBoundsInLocal().getWidth();
        double höhe = canvas.getBoundsInLocal().getHeight();

        // Wie viel ein Pixel entspricht.
        double pixelR = (maxR - minR) / breite;
        double pixelI = (maxI - minI) / höhe;

        // Die fortlaufenden Punkte
        double cr = minR;
        double ci = minI;
        double color;

        System.out.println(breite +" "+ höhe);
        System.out.println(pixelR +" "+ pixelI);

        // Die Haupt-Schleife
        for (int i = 0; i < breite; i++) {
            System.out.println(i);
            for (int j = 0; j < höhe; j++) {

                ci += pixelI;

                color = istInMenge(cr, ci);
                if(color==maxIterationen) color = 0.0;

                gc.setStroke(new Color(color/maxIterationen, 0, 0,1));
                gc.strokeLine(i, j, i, j);

            }
            cr += pixelR;
            ci = minI;
        }
    }
}
