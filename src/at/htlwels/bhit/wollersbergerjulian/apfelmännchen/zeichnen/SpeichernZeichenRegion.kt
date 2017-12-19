package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.zeichnen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.GlobalerThreadManager
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.berechneBereich
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.control.ZeichenflächeController
import javafx.application.Platform
import javafx.embed.swing.SwingFXUtils
import javafx.scene.control.Label
import javafx.scene.image.WritableImage
import javafx.stage.FileChooser
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

// Created by julian on 03.09.17.
/**
 * Diese ZeichenRegion berechnet ein Bild
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
class SpeichernZeichenRegion(
        private val controller: ZeichenflächeController
) : ZeichenRegion() {
    private val eingaben = controller.eingaben

    /** Wie bekommt man den richtigen Pfad?  */
    private val bilderOrdner = File(System.getProperty("user.home")
            + File.separator + "Bilder/Screenshots/Apfelmännchen")

    override fun berechneBild() {
        berechneBildUndSpeichere()
    }

    /**Beauftragt den GlobalerThreadManager, das Bild
     * zu Berechnen. Anschließend wird [speichereBild]
     * aufgerufen. */
    fun berechneBildUndSpeichere() {
        // Kleines Lade-Dingsbums :)
        controller.setLadeDingsbums(Label("Bild wird berechnet..."))

        /* Man nehme den Bereich, der angezeigt wird
         * und die Eingaben für Breite und Höhe.
         * Dann noch entzerren. */
        val imageKoordsys = DoppelKoordinatenSystem(
                controller.globalesKoordsys.kBereich,
                eingaben.eingabeSpeichernBreite,
                eingaben.eingabeSpeichernHöhe
        ).entzerre()
        val parameter = eingaben.eingabeParameter

        val neuesImage = WritableImage(imageKoordsys.breite.toInt(), imageKoordsys.höhe.toInt())
        val pixelWriter = neuesImage.pixelWriter

        // Beauftrage Thread mit der Berechnung.
        GlobalerThreadManager.berechne(Runnable {
            berechneBereich(
                    imageKoordsys,
                    parameter,
                    pixelWriter::setArgb
            )

            /** Der Speichern-Dialog sollte im
             * Fx-Application-Thread gemacht werden. */
            Platform.runLater {
                // Entferne Lade-Dingsbums
                controller.setLadeDingsbums(null)
                speichereBild(neuesImage)
            }
        })
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
            println("Screenshot fehlgeschlagen. selectedFile=" + selectedFile)

    }

    override fun reset() {
    }

    override fun registerEventHanders() {
    }

    override fun layoutChildren() {
    }
}