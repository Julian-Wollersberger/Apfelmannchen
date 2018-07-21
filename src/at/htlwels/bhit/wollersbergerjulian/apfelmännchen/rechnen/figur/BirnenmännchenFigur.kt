package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.figur


// Created by julian on 21.07.18.
/**
 * @see istInMenge
 */
class BirnenmännchenFigur: Figur() {

    /**
     * Dies ist die Formel für z³+c statt z²+c.
     * Erzeugt ein anderes schönes Muster.
     * https://youtu.be/qhbuKbxJsk8?t=5m52s
     *
     * @see ApfelmännchenFigur.istInMenge
     */
    override fun istInMenge(cr: Double, ci: Double, maxIter: Int, maxDistanz: Double): Double {
        var zr = cr
        var zi = ci
        var zrtemp: Double
        val maxDistanzQuad = maxDistanz * maxDistanz

        // Die magische Schleife!
        var i: Int = 0
        while (i < maxIter && zr*zr + zi*zi < maxDistanzQuad) {
            // Hier ist der Unterschied zum Apfelmännchen
            zrtemp = zr*zr*zr - 3*zr*zi*zi + cr
            zi = 3*zr*zr*zi - zi*zi*zi + ci
            zr = zrtemp
            i++
        }

        var feinjustierung: Double = maxDistanzQuad / (zr*zr + zi*zi)
        feinjustierung = Math.max(Math.min(feinjustierung, 0.9999), 0.0)

        return i + feinjustierung
    }

    override fun toString(): String {
        return "Birnenmännchen"
    }
}