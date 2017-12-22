package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.farbe

import javafx.scene.paint.Color


// Created by julian on 22.12.17.
/**
 * Ein abstrakter Farbalgorithmus.
 */
abstract class FarbAlgorithmus {
    /** @return Farbe im ArgbInt-Format. */
    abstract fun berechneFarbe(iterationen: Int, maxIterationen: Int, grundfarbe: Color): Int



    //-------------- Konvertierung --------------

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