package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.anzeigen;

import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.rechnen.Punkt;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by julian on 24.05.17.
 *
 * Das ist der Canvas, auf dem das Apfelmännchen gezeichnet werden soll.
 * Die ganzen Größen sind hier enthalten.
 * Die Methoden nehmen die komplexen Koordinaten an und rechnen sie entsprechend
 * in Pixel-Koordinaten um.
 */
public class KomplexesCanvas extends Canvas{

    /* Der Bereich, in dem die komlexen Werte sind. */
    private double minR;
    private double maxR;
    private double minI;
    private double maxI;

    /* Wie viel ein Pixel in komplexen Werten entspricht.
    * Wird im Konstruktor berechnet. */
    private double deltaR;
    private double deltaI;

    /* Wird für die Berechnung der Farbe benötigt. */
    private int maxIterationen;
    private Color grundfarbe;

    /* Braucht man einen neuen gc, wenn sich die Auflösung ändert? */
    private GraphicsContext gc;

    public KomplexesCanvas(double breite, double minR, double maxR, double minI, double maxI, int maxIterationen, Color grundfarbe) {
        this.minR = minR;
        this.maxR = maxR;
        this.minI = minI;
        this.maxI = maxI;
        this.maxIterationen = maxIterationen;
        this.grundfarbe = grundfarbe;

        setzeBreiteUndHöheUndDeltaRI(breite);
        gc = this.getGraphicsContext2D();
    }


    //TODO KomplexZuPixel
    //TODO PixelZuKomplex

    /**Zeichnet einen Punkt auf dem Canvas.
     * @param r Realteil
     * @param i Imaginärteil
     * @param iterationen Die Anzahl der Iterationen, bis der Punkt abgehaut ist. Für die
     * Berechnung der Farbe. Wenn die Iteration größergleich maxIterationen ist, wird die
     * Grundfarbe verwendet. */
    public void zeichnePunkt(double r, double i, int iterationen)
    {
        //TODO Formel für die Farbe ändern. HSV, quadratisch?, bei Verdopplung einmal rundum um Farbkreis.
        Color color;
        if(iterationen==maxIterationen) color = grundfarbe;
        else color = new Color(0, ((double)(maxIterationen - iterationen))/maxIterationen, 0,1);
        gc.setStroke(color);

        gc.strokeLine((r-minR)/deltaR, (i-minI)/deltaI, (r-minR)/deltaR, (i-minI)/deltaI);
    }

    public void zeichnePunkt(Punkt p){
        zeichnePunkt(p.cr, p.ci, p.iterationen);
    }

    /**Setzt die Breite und berechnet für die Höhe den richtigen Wert.
     * Die Abstände der komplexen Werte werden aktualisiert. */
    public void setzeBreiteUndHöheUndDeltaRI(double breite)
    {
        super.setWidth(breite);
        super.setHeight(breite * berechneSeitenverhältnis());

        deltaR = (maxR - minR) / breite;
        deltaI = (maxI - minI) / (breite * berechneSeitenverhältnis());
    }

    /**Berechnet das Verhältnis von Höhe zu Breite,
     * damit die komplexen Koordinaten ein 1:1 Verhältnis haben.<br><br>
     * höhe = breite * verhältnis <br>
     * breite = höhe / verhältnis*/
    public double berechneSeitenverhältnis()
    {
        return (maxI - minI)/(maxR - minR);
    }

    public double getDeltaR() {
        return deltaR;
    }
    public double getDeltaI() {
        return deltaI;
    }
    public double getMinR() {
        return minR;
    }
    public double getMaxR() {
        return maxR;
    }
    public double getMinI() {
        return minI;
    }
    public double getMaxI() {
        return maxI;
    }
    public int getMaxIterationen() {
        return maxIterationen;
    }

    @Override
    public String toString() {
        return "KomplexesCanvas{" +
                "minR=" + minR +
                ", maxR=" + maxR +
                ", minI=" + minI +
                ", maxI=" + maxI +
                ", deltaR=" + deltaR +
                ", deltaI=" + deltaI +
                ", maxIterationen=" + maxIterationen +
                ", grundfarbe=" + grundfarbe +
                '}';
    }
}
