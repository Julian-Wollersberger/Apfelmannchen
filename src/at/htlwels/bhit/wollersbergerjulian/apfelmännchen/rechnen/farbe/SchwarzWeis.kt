package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.farbe

import javafx.scene.paint.Color


// Created by julian on 22.12.17.
/**
 * Was innerhalb der Mandelbrotmenge ist, ist schwarz, alles andere weis.
 */
class SchwarzWeis : FarbAlgorithmus() {
    override fun berechneFarbe(iterationen: Int, maxIterationen: Int, grundfarbe: Color, feinjustierung: Double): Int {
        if(iterationen == maxIterationen)
            return colorToArgbInt(0.0, 0.0, 0.0, 1.0)
        else
            return colorToArgbInt(1.0, 1.0, 1.0, 1.0)
    }

    override fun toString(): String {
        return "Schwarzweis"
    }
}