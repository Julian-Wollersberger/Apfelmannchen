package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import java.util.stream.IntStream


// Created by julian on 08.07.18.
/**
 * TODO Description
 * Ein Versuch, [istInMenge] auf der GPU laufen zu lassen.
 *
 * http://on-demand.gputechconf.com/gtc/2016/presentation/s6346-kazuaki-ishizaki-gpu-programming-java-programmers.pdf
 */
class GpuBeschleuniger {

    fun berechneBild(
            koordsys: DoppelKoordinatenSystem,
            args: ApfelmännchenParameter,
            zeichnePunkt: (x: Int, y: Int, argb: Int) -> Unit
            // runLater
    ) {
        val breite = koordsys.breite.toInt()
        val size = breite * koordsys.höhe.toInt()
        val bild = IntArray(size)

        //TODO cr und ci direkt aus dem Index ausrechnen, ohne diese Arrays.
        val crArray = DoubleArray(size)
        val ciArray = DoubleArray(size)

        // Zuerst die Arrays befüllen.
        /* Das Bild ist ein 2D-Array, aber hier muss
         * es in einem 1D-Array gespeichert werden.
         * array[width * row + col] = value; */
        fürJedenPunkt(koordsys, { x, y, cr, ci ->
            crArray[breite * y + x] = cr
            ciArray[breite * y + x] = ci
        })

        // Die Berechnung des Apfelmännchens.
        // Hoffentlich ist .parallel() so optimiert, das es die
        // GPU verwendet. -> Nop.
        IntStream.range(0,size).parallel().forEach {

            bild[it] = istInMengeAlt(crArray[it], ciArray[it], args.maxIterationen, args.maxDistanz)

            /* Die Komplexität von istInMenge könnte ein Problem sein.
             * Eigentlich muss nur die Schleife darin parallelisiert werden,
             * aber nicht das Farbe berechnen. */
        }

        bild.forEachIndexed { index, iterationen ->
            val farbe = args.farbAlgorithmus.berechneFarbe(iterationen, args.maxIterationen, args.grundfarbe, 0.0)
            zeichnePunkt(index%breite, index/breite, farbe)
        }
    }
}