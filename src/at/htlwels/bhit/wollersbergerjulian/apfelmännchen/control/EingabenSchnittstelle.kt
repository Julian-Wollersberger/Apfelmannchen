package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.Bereich
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
    var eingabeBereich: Bereich
        get() = eingabenController.leseBereich()
        set(value) = eingabenController.setzeBereichTexte(value)

    var eingabeParameter: ApfelmännchenParameter
        get() = ApfelmännchenParameter(
                eingabeMaxIterationen,
                eingabeMaxDistanz,
                eingabeGrundfarbe)
        set(value) {
            eingabeMaxIterationen = value.maxIterationen
            eingabeMaxDistanz = value.maxDistanz
            eingabeGrundfarbe = value.grundfarbe }

    var eingabeMaxIterationen: Int
        get() = eingabenController.leseMaxIterationen()
        set(value) = eingabenController.setzeMaxIterationenText(value)

    var eingabeMaxDistanz: Double
        get() = eingabenController.leseMaxDistanz()
        set(value) = eingabenController.setzeMaxDistanzText(value)

    //TODO Grundfarbe eingeben können
    var eingabeGrundfarbe: Color
        get() = StandardwerteEingabe.GRUNDFARBE
        set(value) {}

    var eingabeAnzahlThreads: Int
        get() = eingabenController.leseAnzahlThreads()
        set(value) = eingabenController.setzeAnzahlThreadsText(value)

    var eingabeSpeichernBreite: Double
        get() = eingabenController.leseBreite()
        set(value) = eingabenController.setzeBreiteText(value)

    var eingabeSpeichernHöhe: Double
        get() = eingabenController.leseHöhe()
        set(value) = eingabenController.setzeHöheText(value)
}
