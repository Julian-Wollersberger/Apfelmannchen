package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.zeichenfläche

import javafx.scene.layout.Region


//Created by julian on 18.07.17.
/**
 * # Hier wird gezeichnet.
 * In einer ZeichenRegion wird eine mathematische
 * Funktion (z.B. Mandelbrotmenge) dargestellt. Sie
 * rechnet zwischen den kartesischen Koordinaten, die
 * für die Berechnungen verwendet werden und
 * den Pixel-Koordinaten zum Anzeigen um.
 *
 * Es ist eine Region und kein Pane, damit andere Klassen
 * die Children nicht verändern dürfen.
 *
 *  Interne Werte sollen sich nie automatisch ändern,
 *  wenn sich das Layout ändert, sondern nur, wenn es
 *  eine Unterklasse will.
 */
abstract class ZeichenRegion : Region() {

    /** Die Zeichenfläche soll zurückgesetzt werden und neu
     * berechnet werden. */
    abstract fun reset()

    /********  Must-Override
     * A Region subclass must override
     * [computePrefWidth][.computePrefWidth],
     * [computePrefHeight][.computePrefHeight], and
     * [layoutChildren][.layoutChildren]. */
    abstract override fun layoutChildren()

    /** Die bevorzugte Größe ist die minimale Größe, damit ein AnchorPane
     * es verkleinern kann.  */
    override fun computePrefWidth(height: Double): Double {
        // entspricht Insets
        return super.computeMinWidth(height)
    }
    /** Die bevorzugte Größe ist die minimale Größe, damit ein AnchorPane
     * es verkleinern kann.  */
    override fun computePrefHeight(width: Double): Double {
        // entspricht Insets
        return super.computeMinHeight(width)
    }

    /** Gibt die width zurück, den der
     * Inhalt dieser Region zur Verfügung hat.
     * Also insets weggerechnet. */
    fun getContentWidth(): Double {
        return width - insets.left - insets.right
    }
    fun getContentHeight(): Double {
        return height - insets.top - insets.bottom
    }
}

