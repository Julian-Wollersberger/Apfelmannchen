package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.farbe

import javafx.scene.paint.Color

// Created by julian on 22.12.17.
/**
 * Ein abstrakter Farbalgorithmus.
 */
abstract class FarbAlgorithmus {
    /**
     * @param iterationen Die Anzahl der Iterationen, bis der Punkt abgehaut ist.
     * @param maxIterationen Wenn die Iteration größergleich maxIterationen
     * ist, wird die Grundfarbe verwendet.
     * @param grundfarbe Die Farbe, die der Mittelteil des Apfelmännchens bekommt.
     * @param feinjustierung Wert zwischen 0 und 1. Kann ignoriert werden. Dann 0.0 übergeben.
     * @return Die Farbe als Int im argb-Format
     */
    abstract fun berechneFarbe(iterationen: Int, maxIterationen: Int, grundfarbe: Color, feinjustierung: Double): Int



    //-------------- Konvertierung --------------

    /** Die Farbe wird berechnet mit dem HSV-Farbkreis.
     *
     * Die RGB-Werte folgen einem relativ einfachem Muster:
     * Es ist immer eine Farbe auf 255, eine auf 0 und die Dritte variabel.
     * Nach 60° ändert sich, welche. Siehe Farbauswahl bei GIMP.
     * Schritte:
     * 1. rot max,         grün 0,         blau wird mehr
     * 2. rot wird weniger,grün 0,         blau max
     * 3. rot 0,           grün wird mehr, blau max
     * 4. rot 0,           grün max,       blau wird weniger
     * 5. rot wird mehr,   grün max,       blau 0
     * 6. rot max,         grün weniger,   blau 0
     *
     * @param hue Die Rotation im Farbkreis. Im Bereich [0 bis 1]
     *      0 == 0°, 1 == 360°
     */
    fun HsvHueToArgb(hue: Double): Int {
        val color: Int
        if (hue < 0) throw IllegalArgumentException("Hue kleiner 1: $hue")
        else if (hue < 1.0 / 6) color = colorToArgbInt(1.0, 0.0, hue * 6, 1.0)
        else if (hue < 2.0 / 6) color = colorToArgbInt(1 - (hue - 1.0 / 6) * 6, 0.0, 1.0, 1.0)
        else if (hue < 3.0 / 6) color = colorToArgbInt(0.0, (hue - 2.0 / 6) * 6, 1.0, 1.0)
        else if (hue < 4.0 / 6) color = colorToArgbInt(0.0, 1.0, 1 - (hue - 3.0 / 6) * 6, 1.0)
        else if (hue < 5.0 / 6) color = colorToArgbInt((hue - 4.0 / 6) * 6, 1.0, 0.0, 1.0)
        else if (hue <=6.0 / 6) color = colorToArgbInt(1.0, 1 - (hue - 5.0 / 6) * 6, 0.0, 1.0)
        else throw IllegalArgumentException("Hue größer 1: $hue")

        return color
    }

    /** Kopiert aus WriteableImage.
     * Wandelt ein Color-Objekt in einen Integer-Wert,
     * wie es image.pixelWriter.setArgb brauch.*/
    fun colorToArgbInt(c: Color): Int {
        val a = Math.round(c.opacity * 255).toInt()
        val r = Math.round(c.red * 255).toInt()
        val g = Math.round(c.green * 255).toInt()
        val b = Math.round(c.blue * 255).toInt()
        return (a shl 24 or (r shl 16) or (g shl 8) or b)
    }

    /** Kopiert und angepasst aus WriteableImage.
     * Wandelt vier Farb-Double-Werte in einen Integer-Wert,
     * wie es image.pixelWriter.setArgb brauch.*/
    fun colorToArgbInt(red: Double, green: Double, blue: Double, opacity: Double): Int {
        val a = Math.round(opacity * 255).toInt()
        val r = Math.round(red * 255).toInt()
        val g = Math.round(green * 255).toInt()
        val b = Math.round(blue * 255).toInt()
        return (a shl 24 or (r shl 16) or (g shl 8) or b)
    }

    /** Ein Int wieder zu einem Color-Objekt. */
    fun argbIntToColor(argb: Int): Color {
        val a = (argb shr 24)%256 / 255.0
        val r = (argb shr 16)%256 / 255.0
        val g = (argb shr 8) %256 / 255.0
        val b = (argb shr 0) %256 / 255.0

        return Color(r, g, b, a)
    }
}