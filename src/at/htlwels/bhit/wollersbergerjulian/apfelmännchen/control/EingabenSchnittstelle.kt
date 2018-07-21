package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.Bereich
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.farbe.FarbAlgorithmus
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.figur.ApfelmännchenFigur
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.figur.BirnenmännchenFigur
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.EingabenController
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.StandardwerteEingabe
import javafx.scene.paint.Color

// Created by julian on 06.12.17.

/**
 * Schnittstelle zu den GUI-Eingaben. Für
 * alle Klassen, die außerhalb des View-Package liegen.
 */
class EingabenSchnittstelle(
        val eingabenController: EingabenController
) {
    var bereich: Bereich
        get() = eingabenController.leseBereich()
        set(value) = eingabenController.setzeBereichTexte(value)

    var parameter: ApfelmännchenParameter
        get() = ApfelmännchenParameter(
                maxIterationen,
                maxDistanz,
                grundfarbe,
                farbAlgorithmus,
                BirnenmännchenFigur()) //TODO Nicht hardcodiert
        set(value) {
            maxIterationen = value.maxIterationen
            maxDistanz = value.maxDistanz
            grundfarbe = value.grundfarbe
        }

    var maxIterationen: Int
        get() = eingabenController.leseMaxIterationen()
        set(value) = eingabenController.setzeMaxIterationenText(value)

    var maxDistanz: Double
        get() = eingabenController.leseMaxDistanz()
        set(value) = eingabenController.setzeMaxDistanzText(value)

    //TODO Grundfarbe eingeben können
    var grundfarbe: Color
        get() = StandardwerteEingabe.GRUNDFARBE
        set(value) {}

    var farbAlgorithmus: FarbAlgorithmus
        get() = eingabenController.leseFarbAlgorithmus()
        set(value) = eingabenController.setzeFarbAlgorithmus(value)

    var anzahlThreads: Int
        get() = eingabenController.leseAnzahlThreads()
        set(value) = eingabenController.setzeAnzahlThreadsText(value)

    var speichernBreite: Double
        get() = eingabenController.leseBreite()
        set(value) = eingabenController.setzeBreiteText(value)

    var speichernHöhe: Double
        get() = eingabenController.leseHöhe()
        set(value) = eingabenController.setzeHöheText(value)
}
