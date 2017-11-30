package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.einausgabe

import java.io.*
import java.text.ParseException

// Created by julian on 14.10.17.
/**
 * Schöne Bild-Ansichten sollen speicherbar
 * und wieder ladbar sein.
 *
 * Dazu muss der Bereich und ein Name und Parameter
 * gespeichert werden. Das wird mit einer
 * CSV-Datei gemacht.
 *
 * Intern wird es als Set gespeichert, mit einem
 * eindeutigen Namen.
 *
 * Um abwärtskompaktibel zu sein, soll die Datei
 * eine Versionsnummer haben.
 *
 * Aufbau der Datei: Erste Zeile Spaltenüberschriften, zweite die Version.
 * #kxMin   kxMax   kyMin   kyMax   maxIterationen ...
 * #Version: 5.0
 * Generell sind Zeilen mit # zu Beginn zu ignorieren.
 */
class AnsichtenSpeichern {

    //TODO Eigene Klasse fürs Speichern der Enstellungen, Werte, Pfade, ...
    val standardBereicheDatei = "gespeicherteAnsichten.csv"
    val version5 = "#Version: 5.0"

    /** Lädt die gespeicherten Ansichten aus der
     * übergebene Datei.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ParseException wenn das Format ungültig ist.
     * @throws NumberFormatException wenn eine zu lesende Zahl ungültig ist. */
    fun ladeDatei(datei: File): MutableSet<EintragV5> {
        val reader = BufferedReader(InputStreamReader(FileInputStream(datei)))
        val einträge = HashSet<EintragV5>()

        reader.use {
            var zeile = reader.readLine() // Überschriften
            zeile = reader.readLine()     // Version
            if(!zeile.equals(version5))
                throw ParseException("Nicht unterstützte Version: $zeile", 0)

            zeile = reader.readLine() // Ersten Daten
            while (zeile != null) {
                // Ignoriere Zeilen mit #
                if(!zeile.startsWith('#')) {
                    einträge.add(EintragV5.parseZeile(zeile))
                }
            }
        }

        return einträge
    }

    fun speichereDatei(datei: File, einträge: MutableSet<EintragV5>) {
        val writer = BufferedWriter(OutputStreamWriter(FileOutputStream(datei)))

        writer.use {
            for (eintragV5 in einträge) {
                writer.write(eintragV5.toString())
                writer.write("\n")
            }
        }
    }
}