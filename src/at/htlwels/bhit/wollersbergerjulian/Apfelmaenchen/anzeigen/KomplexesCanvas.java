package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.anzeigen;

import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.model.Bereich;
import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.view.ZeichenFensterController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// Created by julian on 24.05.17.
/**
 * Das ist der Canvas, auf dem das Apfelmännchen gezeichnet werden soll.
 * Die ganzen Größen (Grenzen, maxIterationen) sind hier enthalten.
 * TODO Eigene Daten-Klasse für diese Werte. <br>
 * Außerdem ist hier die Methode zum Zeichnen eines Punktes. <br><br>
 *
 * Diese Klasse erbt von Canvas, und ist damit eine Node.
 * Deshalb sind hier auch Methoden, damit die Größe geändert
 * werden kann. Das tatsächliche Ändern der Größe geschieht in
 * {@link ZeichenFensterController}.<br>
 * TODO in eigene Klasse.
 *
 * <br><br> Besonderheit des Layouts:<br>
 * Darf nicht in die AnchorPane, deren größe überwacht wird wegen
 * {@link StackOverflowError}
 */
public class KomplexesCanvas extends Canvas{

    /* Der Bereich, in dem die komlexen Werte sind. */
    private Bereich bereich;

    /* Wie viel ein Pixel in komplexen Werten entspricht.
    * Wird im Konstruktor berechnet.
    * TODO Ersetzen durch einen Wert, weil sie immer gleich sein sollen, weil es nicht verzerrt ist.
    * Andererseits bleichb es so allgemeiner.*/
    private double deltaR;
    private double deltaI;

    /* Wird für die Berechnung der Farbe benötigt.
    * Die Berechnung erhält maxIterationen außerdem mit dem Getter aus dieser Klasse. */
    private int maxIterationen;
    private Color grundfarbe;

    /* Der Buffer des Canvas für Zeichenbefehle. */
    private GraphicsContext gc;

    /** Konstruktor mit Grenzen, maxIterationen und der Grundfarbe.
     * deltaI und deltaR werden daraus berechnet. */
    public KomplexesCanvas(double breite, Bereich bereich, int maxIterationen, Color grundfarbe) {
        this.bereich = bereich;
        this.maxIterationen = maxIterationen;
        this.grundfarbe = grundfarbe;

        setzeBreiteUndHöheUndDeltaRI(breite);
        gc = this.getGraphicsContext2D();
    }

    /**Zeichnet einen Punkt auf dem Canvas. Übergeben werden die komplexen
     * Koordinaten des zu zeichnenden Punktes, die in die Pixel-Koordinaten
     * umgewandelt werden.
     * @param r Realteil
     * @param i Imaginärteil
     * @param iterationen Die Anzahl der Iterationen, bis der Punkt abgehaut ist. Für die
     * Berechnung der Farbe. */
    public void zeichnePunkt(double r, double i, int iterationen) {
        // Die Farbe des Punktes finden.
        gc.setStroke(berechneFarbe(iterationen));
        /* Den Punkt zeichnen.
         * gc kennt keine Methoden für des Zeichnen eines einzelnen Punktes,
         * daher muss ich eine Linie mit gleichen Start- und Endpunkten zeichnen. */
        double x = (r-bereich.getMinR())/deltaR;
        double y = (i-bereich.getMinI())/deltaI;
        gc.strokeLine(x, y, x,y);
    }


    /**Die Farbe wird berechnet mit dem HSV-Farbkreis.<br>
     * Der HSV-Farbkreis wird sozusagen
     * einmal rundumgegangen mit den ersten 15 Iterationen. Für die
     * zweite Runde werden 31 Iterationen benötigt.<br>
     * Die benötigten Iterationen fürs rundumgehen wachsen
     * exponentiell, da die Iterationen exponentiell gegen unendlich gehen,
     * wenn man sich der Grenzlinie nähert.<br>
     * Die HSV-Werte werden noch in RGB umgewandelt.
     *
     * @param iterationen Die Anzahl der Iterationen, bis der Punkt abgehaut ist.
     * Wenn die Iteration größergleich maxIterationen ist, wird die
     * Grundfarbe verwendet.*/
    public Color berechneFarbe(int iterationen) {
        Color color;
        int runde;
        double fraction;

        if(iterationen==maxIterationen) color = grundfarbe;
        else {

            /* Exponentielles Verhalten. Der HSV-Farbkreis wird sozusagen
             * einmal rundumgegangen mit den ersten 15 Iterationen. Für die
             * zweite Runde werden 31 Iterationen benötigt. */
            iterationen += 8;
            runde = 15;
            while (iterationen >= runde)
                // Bitmuster ist immer nur einsen, somit kein Problem mit MAX_INT >= runde.
                runde = (runde * 2) + 1;

            /* iterationen ist nun zwischen runde/2 und runde.
             * Deshalb zuerst minus runde/2; dadurch ist es zwischen 0 und runde/2.
             * Und nur noch in den Bereich von 0 bis 1 bingen. */
            fraction = (iterationen - runde / 2) / (double) (runde / 2);

            /* Die RGB-Werte folgen einem relativ einfachem Muster:
             * Es ist immer eine Farbe auf 255, eine auf 0 und die Dritte variabel.
             * Nach 60° ändert sich, welche. Siehe Farbauswahl bei GIMP.
             * Schritte:
             * 1. rot max,         grün 0,         blau wird mehr
             * 2. rot wird weniger,grün 0,         blau max
             * 3. rot 0,           grün wird mehr, blau max
             * 4. rot 0,           grün max,       blau wird weniger
             * 5. rot wird mehr,   grün max,       blau 0
             * 6. rot max,         grün weniger,   blau 0*/
            if (fraction < 0) {
                System.out.println("Fraction kleiner 0: " + fraction + " iterationen: " + iterationen + " runde: " + runde);
                color = grundfarbe;
            } else if (fraction < 1.0 / 6) color = new Color(1, 0, (fraction) * 6, 1);
            else if (fraction < 2.0 / 6) color = new Color(1 - (fraction - 1.0 / 6) * 6, 0, 1, 1);
            else if (fraction < 3.0 / 6) color = new Color(0, (fraction - 2.0 / 6) * 6, 1, 1);
            else if (fraction < 4.0 / 6) color = new Color(0, 1, 1 - (fraction - 3.0 / 6) * 6, 1);
            else if (fraction < 5.0 / 6) color = new Color((fraction - 4.0 / 6) * 6, 1, 0, 1);
            else if (fraction <= 6.0 / 6) color = new Color(1, 1 - (fraction - 5.0 / 6) * 6, 0, 1);
            else {
                System.out.println("Fraction größer 1: " + fraction + " iterationen: " + iterationen + " runde: " + runde);
                color = grundfarbe;
            }
        }
        return color;
    }

    /**Setzt die Breite und berechnet für die Höhe den richtigen Wert.
     * Die Abstände der komplexen Werte werden aktualisiert. */
    public void setzeBreiteUndHöheUndDeltaRI(double breite) {
        super.setWidth(breite);
        super.setHeight(breite * berechneSeitenverhältnis());

        deltaR = (bereich.getMaxR() - bereich.getMinR()) / breite;
        deltaI = (bereich.getMaxI()- bereich.getMinI()) / (breite * berechneSeitenverhältnis());
    }

    /**Berechnet das Verhältnis von Höhe zu Breite,
     * damit die komplexen Koordinaten ein 1:1 Verhältnis haben.<br><br>
     * höhe = breite * verhältnis <br>
     * breite = höhe / verhältnis*/
    public double berechneSeitenverhältnis()
    {
        return (bereich.getMaxI() - bereich.getMinI())/(bereich.getMaxR()- bereich.getMinR());
    }
    public static double berechneSeitenverhältnis(double minR, double maxR, double minI, double maxI)
    {
        return (maxI - minI)/(maxR - minR);
    }

    public double getDeltaR() {
        return deltaR;
    }
    public double getDeltaI() {
        return deltaI;
    }
    public Bereich getBereich() {
        return bereich;
    }
    public int getMaxIterationen() {
        return maxIterationen;
    }

    /* Diese Methoden sollten bewirken, dass ein Anchor-Pane
    * diegröße dieses Canvas ändert. Macht es aber nicht, weil
    * Canvas PrefWith und PrefHeight auf die Anzahl der
    * Pixel setzt und AnchorPane nicht darunter geht.*/
    @Override
    public boolean isResizable() {
        return true;
    }
    /* Wird von AnchorPane in layoutChildren() aufgerufen. */
    @Override
    public void resizeRelocate(double x, double y, double width, double height) {
        // Muss ich überschreiben
        this.resize(width, height);
        // In Node: LayoutXY werden gesetzt.
        super.relocate(x,y);
    }
    /* Das Größe Ändern bewirkt, dass das Elternelement ebenfalls die Größe ändert.
    * Erzeugt Stack Overflow, wenn das Canvas ein Kind des AnchorPanes ist! */
    @Override
    public void resize(double width, double height) {
        super.setWidth(width);
        super.setHeight(height);
    }

    @Override
    public String toString() {
        return "KomplexesCanvas{" +
                "bereich=" + bereich +
                ", deltaR=" + deltaR +
                ", deltaI=" + deltaI +
                ", maxIterationen=" + maxIterationen +
                ", grundfarbe=" + grundfarbe +
                '}';
    }


}
