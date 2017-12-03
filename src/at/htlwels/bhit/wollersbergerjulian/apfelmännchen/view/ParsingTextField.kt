package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view

import javafx.scene.control.TextField
import javax.naming.OperationNotSupportedException

// Created by julian on 03.12.17.
/**
 * Diese Klasse vereinfacht den Umgang mit TextFields, in
 * die der Benutzer etwas eingeben kann, die aber normalerweise
 * einen Standardwert besitzen.
 *
 * Unterstützte Typen T für den Wert sind Int und Double,
 * alle anderen können nicht eingelesen werden.
 */
class ParsingTextField<T> (
        val standardWert: T
// Initialisiert auch den Text.
) : TextField(standardWert.toString()) {

    /** Wenn der Wert nicht aus dem TextField
     * eingelesen werden kann, dann wird der
     * Standardwert verwendet. */
    var wert: T
    get() {
        try {
            if(standardWert is Int)
                return super.getText().toInt() as T
            else if (standardWert is Double)
                return super.getText().toDouble() as T
            else
                throw NumberFormatException("Typ is weder Int noch Double!")
        } catch (e: NumberFormatException) {
            println("Ungültige Eingabe! " + e.message)
            return standardWert
        }

    }
    set(value) {
        super.setText(value.toString())
    }

    /** Vielleicht ein Property bereitstellen? */
}