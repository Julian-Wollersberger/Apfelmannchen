package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.rechnen;

import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.anzeigen.KomplexesCanvas;
import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by julian on 24.05.17.
 *
 * Diese Klasse soll dafür sorgen, das man sieht, wie das Apfelmännchen
 * gezeichnet wird :)
 *
 * Diese Klasse braucht ein KomplexesCanvas und erzeugt Threads, die
 * die Punkte berechnen.
 *
 * Während ein Thread rechnet erzeugt er eine Liste von zu zeichnenden Punkten.
 * In handle(), das von JavaFx für jedes Frame aufgerufen wird (60 fps?), werden
 * die neuen Punkte aller Threads abgeholt und auf den Canvas gezeichnet.
 */
public class CanvasAnimator extends AnimationTimer {

    /*TODO Jeder Thread soll seine eigene Liste bekommen.
    * TODO Spalte für Spalte statt aufteilen. */

    private KomplexesCanvas canvas;
    //ArrayList<ThreadUndInfos> threads = new ArrayList<>();
    private ArrayList<RechenThread> threads = new ArrayList<>();
    private final int maxKapazität = 1000;
    private BlockingQueue<Punkt> queue = new ArrayBlockingQueue<>(maxKapazität);
    private long startzeit = -1;
    private long endzeit = -1;

    public CanvasAnimator(KomplexesCanvas canvas, int anzahlThreads) {
        this.canvas = canvas;

        /* Erzeugen aller Threads. Jeder soll seinen gerechten Anteil haben.
        * //TODO manchmal scheint es mehr zu zeichnen als nötig. Oder weniger durch Rundungsfehler. */
        double minR = canvas.getMinR();
        double teilR = (canvas.getMaxR() - minR)/anzahlThreads;
        double deltaR = canvas.getDeltaR();
        double maxR = minR + teilR/* - deltaR*/;

        for (int i = 0; i < anzahlThreads; i++) {
            threads.add(new RechenThread(minR, maxR, canvas.getMinI(), canvas.getMaxI(),
                    deltaR, canvas.getDeltaI(), canvas.getMaxIterationen(), queue));

            minR += teilR;
            maxR += teilR;

        }
    }

    @Override
    public void start() {
        super.start();
        threads.forEach(RechenThread::start);
    }

    /**Hole die Punkte aus der Queue und stelle sie dar.
     * Wird vom JavaFx Application thread aufgerufen. */
    @Override
    public void handle(long now) {
        Punkt p;
        while ((p=queue.poll()) != null)
        {
            canvas.zeichnePunkt(p.cr, p.ci, p.iterationen);
            //System.out.println("p = " + p);
        }

        prüfeObThreadsNochLaufenUndStoppe();
        //Startzeit eintragen.
        if(startzeit == -1) startzeit = now;
        endzeit = now;
    }

    @Override
    public void stop() {
        super.stop();

        //Die Threads entlassen
        threads.forEach(RechenThread::interrupt);

        //Die Zeit, die er gebraucht hat, ausgeben
        System.out.println("Zeit in ms: "+ ((endzeit - startzeit)/1000000));
    }

    /**Testen, ob die Threads noch laufen, und wenn keiner mehr läuft,
    * also alle fertig sind, dann stoppe die Animation.
    * Dadurch wird handle() später nicht nicht mehr aufgerufen. */
    private void prüfeObThreadsNochLaufenUndStoppe()
    {
        //System.out.println("Handle in "+ Thread.currentThread());
        boolean mindestensEinerLäuft = false;
        for (RechenThread thread : threads) {
            if(thread.isAlive())
                mindestensEinerLäuft = true;
        }
        //Wenn sie nicht mehr laufen, dann stoppe die Animation
        if (mindestensEinerLäuft == false)
            this.stop();
    }
}
