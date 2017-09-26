package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.rechnen;

import java.util.concurrent.BlockingQueue;

/**
 * Created by julian on 24.05.17.
 *
 * Der Thread blockiert, wenn die Queue voll ist.
 */
public class RechenThread extends Thread {

    /* Der Bereich, in dem die komlexen Werte sind. */
    private double minR;
    private double maxR;
    private double minI;
    private double maxI;

    /* Wie viel ein Pixel in komplexen Werten entspricht. */
    private double deltaR;
    private double deltaI;

    private int maxIterationen;

    private BlockingQueue<Punkt> queue;

    public RechenThread(double minR, double maxR, double minI, double maxI,
                        double deltaR, double deltaI, int maxIterationen,
                        BlockingQueue<Punkt> queue) {
        this.minR = minR;
        this.maxR = maxR;
        this.minI = minI;
        this.maxI = maxI;
        this.deltaR = deltaR;
        this.deltaI = deltaI;
        this.maxIterationen = maxIterationen;
        this.queue = queue;
    }

    @Override
    public void run()
    {
        //System.out.println(this.currentThread() +" gestartet");
        //System.out.println(minR +" "+ maxR+" "+ deltaR);

        int iterationen;
        // Die Haupt-Schleife
        //TODO Statt +=
        for (double cr = minR; cr < maxR; cr += deltaR) {
            //TODO Liste für eine Spalte erstellen

            for (double ci = minI; ci < maxI; ci += deltaI)
            {
                //Das ist die ganze Rechen-Zauberei.
                iterationen = istInMenge(cr, ci);
                //Der Thread blockiert, wenn die Queue voll ist.
                try {
                    queue.put(new Punkt(cr, ci, iterationen));
                } catch (InterruptedException e) {
                    this.stop();
                }
            }
        }
    }

    public int istInMenge(double cr, double ci)
    {
        int maxIter = maxIterationen;
        double zr = cr;
        double zi = ci;
        double zrtemp;
        int i;

        for (i = 0; i < maxIter && zr*zr+zi*zi <200; i++) {
            zrtemp =  zr*zr - zi*zi +cr;
            zi = 2*zr*zi + ci;
            zr = zrtemp;
        }

        return i;
    }

    public BlockingQueue<Punkt> getQueue() {
        return queue;
    }
}
