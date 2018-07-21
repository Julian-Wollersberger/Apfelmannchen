package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.farbe.FarbAlgorithmus
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.figur.Figur
import javafx.scene.paint.Color

// Created by julian on 31.08.17.
/**
 * Fasst die drei Werte maxIterationen, maxDistanz und grundfarbe zusammen,
 * damit im GUI-Code nur eine Zeile statt überall drei verwendet werden kann,
 * wenn die Werte nur weitergereicht werden müssen.
 */
data class ApfelmännchenParameter(
        val maxIterationen: Int,
        val maxDistanz: Double,
        val grundfarbe: Color,
        val farbAlgorithmus: FarbAlgorithmus,
        val figur: Figur
)