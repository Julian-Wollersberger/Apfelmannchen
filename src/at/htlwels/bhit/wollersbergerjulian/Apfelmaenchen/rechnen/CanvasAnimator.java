package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.rechnen;

import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.anzeigen.KomplexesCanvas;
import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.model.Bereich;
import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.model.PunktListe;
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
 * Während ein Thread rechnet, erzeugt er zu zeichnende Punkte.
 * In handle(), das von JavaFx für jedes Frame aufgerufen wird (60 fps?), werden
 * diese Punkte aller Threads abgeholt und auf den Canvas gezeichnet.
 *
 */
public class CanvasAnimator extends AnimationTimer {

    private KomplexesCanvas canvas;
    private ArrayList<RechenThread> threads = new ArrayList<>();
    private BlockingQueue<Double> spaltenQueue;
    private BlockingQueue<PunktListe> punkteQueue;
    private long startzeit = -1;
    private long endzeit = -1;

    /* Erzeugen aller Threads. */
    public CanvasAnimator(KomplexesCanvas canvas, int anzahlThreads) {
        this.canvas = canvas;
        Bereich bereich = canvas.getBereich();

        // Größe der Queues festlegen
        int anzahlSpalten = (int) ((bereich.getMaxR()-bereich.getMinR())/canvas.getDeltaR());
        spaltenQueue = new ArrayBlockingQueue<>(anzahlSpalten);
        punkteQueue = new ArrayBlockingQueue<>(anzahlSpalten);

        Berechnung.fülleSpaltenQueue(bereich.getMinR(), canvas.getDeltaR(), anzahlSpalten, spaltenQueue);

        // Die Threads erstellen
        for (int i = 0; i < anzahlThreads; i++) {
            threads.add( new RechenThread(bereich.getMinI(), bereich.getMaxI(), canvas.getDeltaI(), canvas.getMaxIterationen(), spaltenQueue, punkteQueue));
        }
    }

    @Override
    public void start() {
        super.start();
        System.out.println("Animation gestartet um "+ System.currentTimeMillis());
        threads.forEach(RechenThread::start);
    }

    /**Hole die Punkte aus der Queue und stelle sie dar.
     * Wird vom JavaFx Application thread aufgerufen.
     *
     * */
    @Override
    public void handle(long now) {
        //Start- und Endzeit merken.
        if(startzeit == -1) startzeit = now;
        endzeit = now;

        //Um sicherzustellen, dass zwischendurch auch mal gezeichnet wird.
        //Anscheinend ist now nicht das selbe wie System.currentTimeMillis()
        long frameStart = System.currentTimeMillis();
        //System.out.println(now +" "+ frameStart);

        /*Hole die Punkte aus der Queue und stelle sie dar. Alle 20 ms
        * mache eine Pause, sodass das Canvas rendert. */
        PunktListe punktSpalte;
        while ((System.currentTimeMillis()-frameStart)<20  &&  (punktSpalte=punkteQueue.poll()) != null) {
            punktSpalte.forEach(canvas::zeichnePunkt);
        }

        prüfeObThreadsNochLaufenUndStoppe();
    }

    /** Alle Threads werden entlassen und die Zeit ausgegeben. */
    @Override
    public void stop() {
        super.stop();
        //Die Threads entlassen
        threads.forEach(RechenThread::interrupt);
        System.out.println("Dauer der Animation: "+ ((endzeit - startzeit)/1000000) +"ms");
    }

    /**Testen, ob die Threads noch laufen, und wenn keiner mehr läuft,
    * also alle fertig sind, dann stoppe die Animation.
    * Dadurch wird handle() später nicht nicht mehr aufgerufen.
     *
     * Wenn noch Elemente in der Queue zum Anzeigen sind, dann stoppe nicht. */
    private void prüfeObThreadsNochLaufenUndStoppe()
    {
        //Wenn noch Elemente in der Liste zum Anzeigen sind, dann stoppe nicht.
        if(punkteQueue.peek() == null) {
            //System.out.println("Handle in "+ Thread.currentThread());
            boolean mindestensEinerLäuft = false;
            for (RechenThread thread : threads) {
                if (thread.isAlive())
                    mindestensEinerLäuft = true;
            }
            //Wenn sie nicht mehr laufen, dann stoppe die Animation
            if (! mindestensEinerLäuft)
                this.stop();
        }
    }


}
