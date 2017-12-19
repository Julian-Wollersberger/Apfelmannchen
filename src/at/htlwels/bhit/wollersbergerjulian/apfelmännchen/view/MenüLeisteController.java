package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by julian on 02.09.17.
 */
public class MenüLeisteController implements Initializable {

    /** Muss gesetzt werden! */
    private RootLayoutController rootLayoutController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /** Speichert ein Bild der Zeichenfläche
     * mit der in den Eingaben spezifizierte Größe. */
    @FXML
    private void speichereBild(ActionEvent event) {
        rootLayoutController.getZeichenflächeController().speichereZeichenfläche();
    }

    @FXML
    private void resetZeichenregion(ActionEvent event) {
        rootLayoutController.getZeichenflächeController().resetZeichenRegion();
    }

    /** Stößt die Berechnung an.
     * Um mit aktualisierten Eingaben zu arbeiten. */
    @FXML
    private void neuBerechnen(ActionEvent event) {
        rootLayoutController.getZeichenflächeController().berechneBild();
    }

    void setRootLayoutController(RootLayoutController rootLayoutController) {
        this.rootLayoutController = rootLayoutController;
    }
}
