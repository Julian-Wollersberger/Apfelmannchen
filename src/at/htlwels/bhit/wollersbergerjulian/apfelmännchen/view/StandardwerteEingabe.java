package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view;

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter;
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.Bereich;
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.GlobalerThreadManager;
import javafx.scene.paint.Color;

// Created by julian on 02.07.17.
/**
 * Alle Standardwerte, die bei der Berechnung verwendet
 * werden. Sie werden zum Programmstart in die
 * Eingabefelder eingetragen und verwendet,
 * wenn Eingaben ungültig sind.
 *
 * Diese Klasse sollte nur für die Controller zugängig sein.
 * (Ich brauche sie aber für die Tests)
 * Dadurch müssen überall anders die Werte
 * verwendet werden, die das GUI liefert.
 */
public class StandardwerteEingabe {

    public static final double MIN_R = -2.1;
    public static final double MAX_R = 1;
    public static final double MIN_I = -1.2;
    public static final double MAX_I = 1.2;

    public static final int MAX_ITERATIONEN = 1000;
    public static final double MAX_DISTANZ = 10;
    public static final Color GRUNDFARBE = Color.WHITE;

    //Wert ist seperat in GlobalerThreadManager!
    public static final int ANZAHL_THREADS = GlobalerThreadManager.STANDARD_ANZAHL_THREADS;

    public static final double SPEICHERN_BREITE = 1920;
    public static final double SPEICHERN_HÖHE = 1080;

    /** Erzeugt eine Bereichs-Klasse mit den
     * Standard-Grenzen des Apfelmännchens. */
    public static Bereich standardBereich() {
        return new Bereich(MIN_R, MAX_R, MIN_I, MAX_I);
    }

    /** Zum testen. */
    public static ApfelmännchenParameter parameter() {
        return new ApfelmännchenParameter(MAX_ITERATIONEN, MAX_DISTANZ, GRUNDFARBE);
    }
}
