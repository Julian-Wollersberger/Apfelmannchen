package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem


// Created by julian on 06.12.17.
/**
 * TODO Description
 *
 * Im Grunde reicht das Strategy Pattern nicht.
 * Factory Pattern?
 *
 * Wunderschöne Matheaufgabe vom Corbinian :)
 * (x-5)(3x-3) = 3(x-9) + 3x²
 * 3x² -3x -15x +15 = 3x -27 +3x²   |-3x²
 * -18x +15 = 3x -27    |-3x -15
 * -21x = -42
 * x = 2
 */
abstract class AbstrakteRechenStrategie(
        koordsys: DoppelKoordinatenSystem,
        params: ApfelmännchenParameter
) {
    abstract fun rechne()
}