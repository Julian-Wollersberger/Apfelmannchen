package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.rechnen;

import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.model.PunktListe;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

// Created by julian on 27.06.17.
/**
 * In dieser Klasse befindet sich der mathematische Algorithmus für die
 * Berechnung der Mandelbrot-Menge.
 */
public class Berechnung {
    // Die Distanz vom Ursprung (0+0i), ab der ein Punkt als gegen undendlich gehend gilt.
    public static final int STANDARD_MAX_DISTANZ = 10;

    public static void fülleSpaltenQueue(double minR, double deltaR, int anzahlSpalten, BlockingQueue<Double> spaltenQueue) {
        // Diese Spalten sollen ausgerechne werden.
        double tempR;
        for (int i = 0; i < anzahlSpalten; i++) {
            // Statt += erhöht Genauigkeit.
            tempR = minR + (i*deltaR);
            spaltenQueue.add(tempR);
        }
    }

    /**TODO beschreiben, wie die parallelisierung gemacht wird. */
    public static void berechnePunkte(double minI, double maxI, double deltaI, int maxIterationen, BlockingQueue<Double> columsQueue, BlockingQueue<PunktListe> punkteQueue)
            throws InterruptedException
    {
        int iterationen;
        Double CR;
        PunktListe spalte;
        // Das +2 wird anscheinend wegen Rundungsfehler gebraucht.
        int anzahlPunkte = (int) ((maxI-minI)/deltaI) +2;

        // Haupt-Schleife. Für alle Spalten.
        while ((CR = columsQueue.poll()) != null) {
            spalte = new PunktListe(anzahlPunkte);

            //Für alle Punkte in der Spalte
            int j = 0;
            for (double ci = minI; ci < maxI; ci=minI+ j*deltaI) {
                //Das ist die ganze Rechen-Zauberei.
                iterationen = istInMenge(CR, ci, maxIterationen, STANDARD_MAX_DISTANZ);
                spalte.add(CR,ci,iterationen);
                j++;
            }

            // Berechnete Liste von Punkten in die Queue geben.
            // If it was not successfull (time ran out)
            if (!punkteQueue.offer(spalte, 1, TimeUnit.SECONDS))
                System.out.println(Thread.currentThread() +" schaffte es nicht rechzeitig, " +
                        "die Ergebnisse in die Liste zu geben. ");
        }
    }

    /**Diese Iteration ist das <b>Herzstück der Mandelbrotmenge</b>.
     * Für einen komplexen Punkt c wird ermittelt, ob diese Folge
     * gegen unendlich oder gegen 0 geht:
     * <code>
     * <br>     z(0) = c
     * <br>     z(n) = (z(n-1))² + c
     * </code>
     * <br><br>
     * Da ein Computer nicht unendlich viel Rechenleistung hat, kann nicht exakt
     * ermittelt werden, ob ein Punkt wirklich gegen 0 oder unendlich geht.
     * <br>Deshalb wird ein Näherungsverfahren eingesetzt:
     * <br>Sobald der Betrag (Distanz; |z|) nach vielen Iterationen 2 (?) überschreitet, kann es nicht mehr gegen
     * 0 gehen. Aber genau am Rand braucht es unendlich viele Iterationen, bis man entscheiden kann. Deshalb
     * wird eine maximale Anzahl an Iterationen verwendet; wenn es nach maxIterationen nicht klar ist, dann wird
     * angenommen, die Folge geht gegen 0.
     *
     * @param cr Realteil des Anfangspunktes
     * @param ci Imaginärteil des Anfangspunktes
     * @param maxIter Anzahl Iterationen, bis angenommen wird, die Folge geht gegen 0.
     *                Je höher, desto genauer an den Rändern.
     * @param maxDistanz Betrag von z, ab dem die Folge gegen unendlich geht. Muss mindestens
     *                   2 sein; höhere Werte liefern genauere Farben weit außen.
     * @return Anzahl Iterationen, bis die Folge maxDistanz überschritten hat.
     *         Daraus wird die Farbe errechnet.
     */
    public static int istInMenge(double cr, double ci, int maxIter, double maxDistanz) {
        double zr = cr;
        double zi = ci;
        double zrtemp;
        int i;
        // Distanz wird mit Pythagoras berechnet: dist² = zr² + zi²
        // Da Wurzel berechnen langsam ist, wird stattdessen Vergleichswert quadriert.
        maxDistanz = maxDistanz*maxDistanz;

        for (i = 0; i < maxIter && zr*zr+zi*zi < maxDistanz; i++) {
            zrtemp =  zr*zr - zi*zi +cr;
            zi = 2*zr*zi + ci;
            zr = zrtemp;
        }

        return i;
    }
}

/*
* Dies ist die Formel für z³+c statt z²+c.
* Erzeugt ein anderes schönes Muster.
* https://youtu.be/qhbuKbxJsk8?t=5m52s
*
* zrtemp = zr*zr*zr - 3*zr*zi*zi +cr;
*  zi = 3*zr*zr*zi - zi*zi*zi +ci;
*  zr = zrtemp;
* */
