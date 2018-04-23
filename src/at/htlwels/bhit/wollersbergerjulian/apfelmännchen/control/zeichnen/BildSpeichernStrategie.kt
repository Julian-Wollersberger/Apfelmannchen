package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control.zeichnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control.ZeichenflächeController
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.BerechnungMultithreaded
import javafx.application.Platform
import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.WritableImage
import javafx.scene.layout.Pane
import javafx.stage.FileChooser
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO


// Created by julian on 13.04.18.
/**
 * Diese Klasse ist zum erstellen eines Bildes
 * zuständig, das gespeichert werden soll.
 *
 * Dies hier ist ziemlich drangepfuscht, wenn diese
 * Klasse grob im weg steht, soll sie lieber gelöscht
 * werden.
 */
internal class BildSpeichernStrategie(
        elternPane: Pane,
        val verwalter: ZeichenStrategienVerwalter,
        val controller: ZeichenflächeController
) : ZeichenStrategie(elternPane) {

    /** Wie bekommt man den richtigen Pfad?  */
    private val bilderOrdner = File(System.getProperty("user.home")
            + File.separator + "Bilder/Screenshots/Apfelmännchen")

    override fun aktualisiere() {
        berechneBildUndSpeichere()
    }

    /**Diese ZeichenRegion berechnet ein Bild
     * mit der Eingebbaren Größe und speichert
     * dieses ins Dateisystem.
     *
     * Eventuell irgendwann durch einen
     * SpeichernController ersetzten, damit alle
     * Effekte, wie z.B. PunktZeichenRegion,
     * mit einer Auswählbaren Größe gespeichert
     * werden können. Mit einem Snapshot
     * geht nur die tatsächliche Größe.
     */
    fun berechneBildUndSpeichere() {

        /* Man nehme den Bereich, der angezeigt wird
         * und die Eingaben für Breite und Höhe.*/
        val imageKoordsys = DoppelKoordinatenSystem(
                controller.globalesKoordsys.kBereich,
                controller.eingaben.speichernBreite,
                controller.eingaben.speichernHöhe
        ).entzerre()

        val neuesImage = WritableImage(imageKoordsys.breite.toInt(), imageKoordsys.höhe.toInt())

        // Beauftrage Thread mit der Berechnung.
        val multi = BerechnungMultithreaded()
        multi.teileAufThreadsAuf(
                controller.eingaben.anzahlThreads,
                imageKoordsys,
                controller.eingaben.parameter
        )
        // Wenn Bild fertig, dann speichern
        Thread{
            multi.waitForAll()
            Platform.runLater {
                // Schreibt alles aufs Image
                BildAnimierer(neuesImage, multi.queue).handle(0L)
                speichereBild(neuesImage)
            }

        }.start()
    }


    /** Öffnet einen Datei-Speichern-Dialog,
     * wo der Benutzer auswählen kann, wo
     * die Datei gespeichert wird. */
    fun speichereBild(image: WritableImage) {
        val fileChooser = FileChooser()
        fileChooser.title = "Speichere Apfelmännchen"
        fileChooser.extensionFilters.add(
                //new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
                FileChooser.ExtensionFilter("PNG", "*.png")
        )
        if (bilderOrdner.isDirectory())
            fileChooser.initialDirectory = bilderOrdner

        val selectedFile: File? = fileChooser.showSaveDialog(controller.getOwner())

        if (selectedFile != null) {
            // IrgendwannTODO Ermittle Dateityp
            // Konvertiere und Speichere das Bild
            try {
                val bufferedImage = SwingFXUtils.fromFXImage(image, null)
                ImageIO.write(bufferedImage, "PNG", selectedFile)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else
            println("Speichern fehlgeschlagen. selectedFile=" + selectedFile)
    }

}