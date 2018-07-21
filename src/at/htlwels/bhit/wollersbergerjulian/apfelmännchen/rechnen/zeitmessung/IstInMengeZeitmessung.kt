package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.zeitmessung

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.StandardwerteEingabe
import org.junit.jupiter.api.Test

// Created by julian on 04.12.17.

/**
 * Testet, wie schnell istInMenge rechnet.
 * Wenn der Test läuft, zeigt IntelliJ eine
 * Zeitmessung in ms an.
 *
 * Ergebnis:
 *
 * Bei 1.000.000.000 Schleifendurchläufen braucht mein
 * Laptop gut 4s220ms bis 4s350ms
 * bzw. 4s140ms bis 4s397ms.
 * Es ist außerdem kein Unterschied zwischen
 * Schleife außen oder die in istInMenge drinn zu sein.
 */
/*internal class IstInMengeZeitmessung {
    @Test
    fun bitteKompilierErstAllesSchön() {
        println(istInMenge(0.0, -0.4166666666666667, 1000, 10.0))
    }

    @Test
    fun istInMengeSchleifeZeitmessung(): Int {
        // Ergebnis merken, damit der Compiler das hier nicht einfach weglässt beim optimieren.
        var erg = 0
        for(i in 1..1000000)
            erg += istInMenge(0.0, -0.4166666666666667, 1000, 10.0)

        println(erg)
        return erg
    }
    @Test
    fun istInMengeZeitmessung() {
        // Ergebnis merken, damit der Compiler das hier nicht einfach weglässt beim optimieren.
        val erg = istInMenge(0.0, -0.4166666666666667, 1000000000, 10.0)
        println(erg)
    }
}*/