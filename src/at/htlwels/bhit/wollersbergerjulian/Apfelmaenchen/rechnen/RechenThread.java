package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.rechnen;

import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.model.PunktListe;

import java.util.concurrent.BlockingQueue;

// Created by julian on 24.05.17.
/**
 * Die Berechnung der Punkte wird auf Threads aufgeteilt, denn diese
 * Aufgabe ist wunderschön parallelisierbar.
 *
 * <br><br> Hier wird nur
 * {@link Berechnung#berechnePunkte(double, double, double, int, BlockingQueue, BlockingQueue) berechnePunkte}
 * aufgerufen.
 */
public class RechenThread extends Thread {

    /* Der Bereich, in dem die komlexen Werte sind. */
    private double minI;
    private double maxI;
    /* Wie viel ein Pixel in komplexen Werten entspricht. */
    private double deltaI;
    private int maxIterationen;

    /* Die Queue soll nun nur dafür verwendet werden, dass
       die Threads die Spalten bekommen, die sie berechnen sollen.*/
    private BlockingQueue<Double> columsQueue;
    /* Eine Queue, in der die Ergebnisse kommen. */
    private BlockingQueue<PunktListe> punkteQueue;

    /**@param columsQueue eine Liste der r-Werte, die die Spalten representieren, die ein Thread berechnen soll.*/
    public RechenThread(double minI, double maxI, double deltaI, int maxIterationen,
                        BlockingQueue<Double> columsQueue, BlockingQueue<PunktListe> punkteQueue) {
        this.minI = minI;
        this.maxI = maxI;
        this.deltaI = deltaI;
        this.maxIterationen = maxIterationen;
        this.columsQueue = columsQueue;
        this.punkteQueue = punkteQueue;
    }

    /** Die Berechnung der Punkte. */
    @Override
    public void run() {
        try {
            Berechnung.berechnePunkte(minI, maxI, deltaI, maxIterationen, columsQueue, punkteQueue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.toString() +" ist zu einem Ende gekommen um "+ System.currentTimeMillis());
    }
}
