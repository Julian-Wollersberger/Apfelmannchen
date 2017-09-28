package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view;

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.Bereich;
import javafx.scene.paint.Color;

// Created by julian on 02.07.17.
/**
 * Alle Standardwerte, die bei der Berechnung verwendet
 * werden. Sie werden zum Programmstart in die
 * Eingabefelder eingetragen und verwendet,
 * wenn Eingaben ungültig sind.
 *
 * Klasse ist Package-private und nur für die Controller zugängig.
 * Dadurch müssen in den Paketen rechnen und zeichnen die Werte
 * verwendet werden, die das GUI liefert.
 */
class StandardwerteEingabe {

    static final double MIN_R = -2.1;
    static final double MAX_R = 1;
    static final double MIN_I = -1.2;
    static final double MAX_I = 1.2;

    static final int MAX_ITERATIONEN = 1000;
    static final double MAX_DISTANZ = 10;
    static final Color GRUNDFARBE = Color.WHITE;

    //Wert ist seperat in ThreadManager!
    static final int ANZAHL_THREADS = 4;

    static final double SPEICHERN_BREITE = 1920;
    static final double SPEICHERN_HÖHE = 1080;

    /** Erzeugt eine Bereichs-Klasse mit den
     * Standard-Grenzen des Apfelmännchens. */
    static Bereich standardBereich() {
        return new Bereich(MIN_R, MAX_R, MIN_I, MAX_I);
    }
}
