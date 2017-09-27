package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model

import com.sun.jndi.ldap.Ber

// Created by julian on 11.07.17.
/**
 * Diese Klasse enthält die Grenzen eines
 * kartesischen Koordinatensystems.
 *(kxMin, kxMax, kyMin, kyMax)
 *
 * Außerdem werden die Größen kxSpanne
 * und kySpanne, also wie weit es von
 * Min bis Max ist, ausgerechnet.
 * (Ende minus Anfang.)
 */
data class Bereich(
        val kxMin: Double, val kxMax: Double, val kyMin: Double, val kyMax: Double) {

    /** Betrag; Ende minus Anfang. */
    val kxSpanne = kxMax - kxMin
    /** Betrag; Ende minus Anfang. */
    val kySpanne = kyMax - kyMin

    /** Erzeugt einen skalierten Bereich, bei dem alle Grenzen
     * einfach mit dem übergebenen Faktor multipliziert sind.
     * Weil Mathematik sind auch die Spannen um den selben
     * Faktor skaliert. */
    fun skalliert(zoomFaktor: Double): Bereich {
        return Bereich(kxMin*zoomFaktor, kxMax*zoomFaktor,
                kyMin*zoomFaktor, kyMax*zoomFaktor)
    }

    /** Addiert zu beiden kx deltaKX und zu beiden ky deltaKY. */
    fun verschoben(deltaKX: Double, deltaKY: Double): Bereich {
        return Bereich(kxMin+deltaKX, kxMax+deltaKX, kyMin+deltaKY, kyMax+deltaKY)
    }
}
