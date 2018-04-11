package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.farbe

import javafx.scene.paint.Color


// Created by julian on 11.04.18.
/**
 * Ein einfacherer Algorithmus für die Berechnung.
 * Anstatt einen Logarithmus zu verwenden,
 * wird hier nur modulo angewendet.
 *
 * Dieser Modus sieht weiter herausen noch gut aus,
 * bei hohem Zoom wird es nur noch Rauschen.
 */
class HsvFarbkreisLinear : FarbAlgorithmus() {
    override fun berechneFarbe(iterationen: Int, maxIterationen: Int, grundfarbe: Color): Int {
        var color: Int

        if (iterationen == maxIterationen)
            color = colorToArgbInt(grundfarbe)
        else {
            color = HsvHueToArgb((iterationen % 50) /50.0)
        }
        return color
    }

    override fun toString(): String {
        return "HSV Farbkreis Linear"
    }
}