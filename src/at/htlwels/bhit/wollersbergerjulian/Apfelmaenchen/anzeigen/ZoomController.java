package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.anzeigen;

import javafx.scene.input.*;

/**
 * Created by julian on 02.07.17.
 *
 * Hier sind die Methoden, die das Zoomen behandeln sollen.
 * Es fehlt noch, das das Canvas verschoben wird.
 * Vielleicht gleich umbenennen und Verschieben auch mitbehandeln.
 */
public class ZoomController {

    private double totalerZoomFaktor = 100;
    private KomplexesCanvas canvas;

    private double vorherigesX = 0;
    private double vorherigesY = 0;

    public ZoomController(KomplexesCanvas canvas) {
        this.canvas = canvas;
    }

    /** TODO Das Canvas wird skalliert und so verschoben, das der Zentrum-Punkt da bleibt, wo er war. */
    public void zoome(double faktor, double zentrumX, double zentrumY)
    {
        canvas.setScaleX(faktor);
        canvas.setScaleY(faktor);
    }

    /** Für ein Finger-Wisch-Zoomen
     * TODO Wischen testen */
    public void zoomeBeimWischen(ZoomEvent event)
    {
        if(event.equals(ZoomEvent.ZOOM))
            canvas.setScaleX(event.getZoomFactor());
        canvas.setScaleY(event.getZoomFactor());
    }

    /** Scrollen mit Mausrad.
     * Der Zoom-Faktor wächt nicht konstant. Vielleicht braucht es hier eine
     * exponentielle Formel. */
    public void zoomeBeimScrollen(ScrollEvent event)
    {
        totalerZoomFaktor += event.getDeltaX() + event.getDeltaY();
        if (totalerZoomFaktor < 50) totalerZoomFaktor = 50;

        zoome(totalerZoomFaktor/100, event.getSceneX(),event.getSceneY());
    }

    public void beginneBewegung(MouseEvent  event) {
        vorherigesX = event.getSceneX();
        vorherigesY = event.getSceneY();
    }

    //TODO Passt nicht vom Namen her.
    /** Reagiert auf ein DragEvent. */
    public void bewege(MouseEvent event) {
        if(vorherigesX == 0 || vorherigesY == 0) {
            vorherigesX = event.getSceneX();
            vorherigesY = event.getSceneY();
        } else {
            canvas.relocate(canvas.getLayoutX() + (event.getSceneX() - vorherigesX),
                    canvas.getLayoutY() + (event.getSceneY() - vorherigesY));
            vorherigesX = event.getSceneX();
            vorherigesY = event.getSceneY();
        }
    }

    public void endeBewegung(MouseEvent event) {
        vorherigesX = 0;
        vorherigesY = 0;
    }
}
