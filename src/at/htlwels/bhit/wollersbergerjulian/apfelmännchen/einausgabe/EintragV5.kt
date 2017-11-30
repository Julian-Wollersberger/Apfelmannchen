package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.einausgabe

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.ApfelmännchenParameter
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.Bereich
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.argbIntToColor
import java.text.ParseException

// Created by julian on 14.10.17.
/** 
 * V5 ist die erste Version, denn sie ist
 * nach der Apfelmännchen-Version benannt.
 * Das Trennzeichen ist '\t'.
 *
 * Der Name wird bereinigt von allen
 * Tabulatoren und Neuzeilen.
 */
class EintragV5(
        //Bereich
        val kxMin: Double,
        val kxMax: Double,
        val kyMin: Double,
        val kyMax: Double,
        // ApfelmännchenParameter
        val maxIterationen: Int,
        val maxDistanz: Double,
        val grundfarbeARGB: Int,
        name: String
) {
    /* Alle whitespace character ersetzen: [ \t\n\x0B\f\r] */
    val bereinigterName = name.replace(Regex("\\s")," ")

    /** Ersten beiden Zeilen in der Datei */
    val spaltenBezeichnungen = "#kxMin\tkxMax\tkyMin\tkyMax\tmaxIterationen\tmaxDistanz\tgrundfarbeARGB\tbereinigterName"
    val version = "#Version 5"

    val bereich; get() = Bereich(kxMin,kxMax,kyMin,kyMax)
    val parameter; get() = ApfelmännchenParameter(maxIterationen,maxDistanz, argbIntToColor(grundfarbeARGB))

    /** CSV-String mit \t als Trennzeichen. */
    override fun toString(): String {
        return "$kxMin\t$kxMax\t$kyMin\t$kyMax\t$maxIterationen\t$maxDistanz\t$grundfarbeARGB\t$bereinigterName"
    }

    /** Der Name muss eindeutig sein. */
    override fun equals(other: Any?): Boolean {
        if (other is EintragV5)
            return bereinigterName.equals(other.bereinigterName)
        else return false
    }
    override fun hashCode(): Int {
        return bereinigterName.hashCode()
    }

    companion object {
        fun parseZeile(zeile: String): EintragV5 {
            val felder = zeile.split('\t')
            if (felder.size != 8)
                throw ParseException("Ungültige Anzahl an Einträgen: ${felder.size}", 0)

            return EintragV5(
                    // Bereich
                    felder[0].toDouble(),
                    felder[1].toDouble(),
                    felder[2].toDouble(),
                    felder[3].toDouble(),
                    // Parameter
                    felder[4].toInt(),
                    felder[5].toDouble(),
                    felder[6].toInt(),
                    // Name
                    felder[7]
            )
        }
    }
}