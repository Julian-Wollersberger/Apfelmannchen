package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model


/**
 * Created by julian on 19.08.17.
 */
data class IterationsWerte (
        val zr: Double,
        val zi: Double,
        val iteration: Int
) {
    val distanz: Double
        get() = zr*zr + zi*zi
}