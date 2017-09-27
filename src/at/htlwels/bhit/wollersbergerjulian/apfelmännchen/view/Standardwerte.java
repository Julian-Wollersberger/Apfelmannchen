package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view;

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.Bereich;
import javafx.scene.paint.Color;

/**
 * Created by julian on 02.07.17.
 */
public class Standardwerte {

    public static final int MAX_ITERATIONEN = 1000;
    public static final int ANZAHL_THREADS = 2;
    public static final double DISTANZ = 10;
    public static final double MIN_R = -2.1;
    public static final double MAX_R = 1;
    public static final double MIN_I = -1.2;
    public static final double MAX_I = 1.2;
    public static final Color GRUNDFARBE = Color.WHITE;

    /** Erzeugt eine Bereichs-Klasse mit den
     * Standard-Grenzen des Apfelmännchens. */
    public static Bereich standardGrenzen() {
        return new Bereich(MIN_R, MAX_R, MIN_I, MAX_I);
    }
}
