package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view;

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.MainApp;
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.Bereich;
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.DoppelKoordinatenSystem;
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.zeichenfläche.InteraktiveZeichenRegion;
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.zeichenfläche.PunktZeichenregion;
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view.zeichenfläche.ZeichenRegion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

// Created by julian on 03.07.17.
// Muss Java-Klasse sein, damit es der SceneBuilder kennt.
/**
 * <h1>Zeichenfenster</h1>
 * Das Haupt-Fenster der ganzen Anwendung.
 * Auf diesem Fenster sind die {@link ZeichenRegion}
 * und die Eingaben der Werte.<br>
 * Gemacht im SceneBuilder. <br><br>
 *
 * Das, was nicht im SceneBuilder zum einstellen geht, wird auch
 * hier gemacht. Zum Beispiel die Eingabefelder mit
 * Werten belegen, auslesen setzen; der ZeichenRegion
 * EventListener hinzufügen.
 */
public class HauptfensterController implements Initializable {
    private MainApp mainApp;

    /* Vielleicht eines Tages die Eingabe in eine eigene Klasse?
    * Dann ließe sie sich einfach austauschen für verschiedene Berechnungen. */
    @FXML private GridPane eingabeGridPane;
    @FXML private TextField iterationenEingabe;
    @FXML private TextField threadsEingabe;
    @FXML private TextField minREingabe;
    @FXML private TextField maxREingabe;
    @FXML private TextField minIEingabe;
    @FXML private TextField maxIEingabe;

    /** zeichenRegion ist im AnchorPane ist im StackPane.
     * Das StackPane ist für prefSize zuständig,
     * Das AnchorPane vergrößert und verkleinert die zeichenRegion. */
    @FXML private StackPane zeichenflächeStackPane;
    @FXML private AnchorPane zeichenflächeAnchorPane;
    private ZeichenRegion zeichenRegion;
    private PunktZeichenregion punktZeichenregion;

    private static final double zeichenRegionPrefWidth = 600;
    private static final double zeichenRegionPrefHeight = 450;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setzeTexte();
        //manuelleLayoutKorrekturen() in MainApp

        // Neue Zeichen-Region
        //TODO zwischen Simpel und Interaktiv wählen können
        //zeichenRegion = new SimpleZeichenRegion(this, new DoppelKoordinatenSystem(
        //        Standardwerte.standardGrenzen(), zeichenRegionPrefWidth, zeichenRegionPrefHeight));
        zeichenRegion = new InteraktiveZeichenRegion(this, new DoppelKoordinatenSystem(
                Standardwerte.standardGrenzen(), zeichenRegionPrefWidth, zeichenRegionPrefHeight));

        zeichenRegion.setStyle("-fx-border-style: solid;-fx-border-width: 2px");
        AnchorPane.setLeftAnchor(zeichenRegion, 0.0);
        AnchorPane.setRightAnchor(zeichenRegion, 0.0);
        AnchorPane.setTopAnchor(zeichenRegion, 0.0);
        AnchorPane.setBottomAnchor(zeichenRegion, 0.0);
        zeichenflächeAnchorPane.getChildren().add(zeichenRegion);

        zeichenflächeStackPane.setPrefWidth(zeichenRegionPrefWidth);
        zeichenflächeStackPane.setPrefHeight(zeichenRegionPrefHeight);

        if(zeichenRegion instanceof InteraktiveZeichenRegion) {
            InteraktiveZeichenRegion izr = (InteraktiveZeichenRegion)zeichenRegion;
            zeichenflächeAnchorPane.setOnScroll(izr::zoomeBeimScrollen);
        }

        // PunktZeichenRegion
        punktZeichenregion = new PunktZeichenregion(this);
        AnchorPane.setLeftAnchor(punktZeichenregion, 0.0);
        AnchorPane.setRightAnchor(punktZeichenregion, 0.0);
        AnchorPane.setTopAnchor(punktZeichenregion, 0.0);
        AnchorPane.setBottomAnchor(punktZeichenregion, 0.0);
        zeichenflächeAnchorPane.getChildren().add(punktZeichenregion);
        zeichenflächeAnchorPane.setOnMouseMoved(punktZeichenregion::zeichneIterationen);
        //TODO Es soll vorne sein

        //if(zeichenRegion instanceof SimpleZeichenRegion) {
            /* Zoomen mit Click und Shif-Click :)
            * TODOO nicht über Textboxen Werte austauschen. */
            /*zeichenRegion.setOnMouseClicked(event -> {
                // Ohne Shift: Minimum, mit: Maximum setzen
                if(!event.isShiftDown()) {
                    minREingabe.setText(""+zeichenRegion.wToKX(event.getX()));
                    minIEingabe.setText(""+zeichenRegion.hToKY(event.getY()));
                } else {
                    maxREingabe.setText(""+zeichenRegion.wToKX(event.getX()));
                    maxIEingabe.setText(""+zeichenRegion.hToKY(event.getY()));
                    resetZeichenregion(null);
                }
            });
        }*/
    }

    /** Ein paar Dinge gehen nicht im SceneBuilder zum einstellen.
     * Die Größen sind erst nach stage.show() berechnet, deshalb muss
     * es dannach aufgerufen werden.*/
    public void manuelleLayoutKorrekturen() {
        // Die Zeichenfläche soll neben der Eingabe angezeigt werden.
        AnchorPane.setLeftAnchor(zeichenflächeStackPane, eingabeGridPane.getBoundsInParent().getMaxY());
        //System.out.println("zeichenflächeStackPane setLeftAnchor "+ eingabeGridPane.getBoundsInParent().getMaxY());
    }

    /** Setzt TextFields auf Standardwerte. */
    private void setzeTexte() {
        iterationenEingabe.setText(Integer.toString(Standardwerte.MAX_ITERATIONEN));
        threadsEingabe.setText(Integer.toString(Standardwerte.ANZAHL_THREADS));
        minREingabe.setText(Double.toString(Standardwerte.MIN_R));
        maxREingabe.setText(Double.toString(Standardwerte.MAX_R));
        minIEingabe.setText(Double.toString(Standardwerte.MIN_I));
        maxIEingabe.setText(Double.toString(Standardwerte.MAX_I));
    }

    /** Setzt TextFields auf übergebenen Bereich. */
    public void setzeBereich(Bereich bereich) {
        minREingabe.setText(Double.toString(bereich.getKxMin()));
        maxREingabe.setText(Double.toString(bereich.getKxMax()));
        minIEingabe.setText(Double.toString(bereich.getKyMin()));
        maxIEingabe.setText(Double.toString(bereich.getKyMax()));
    }

    /** Liest die Eingaben in den TextFields und
     * gibt sie als Bereich zurück.
     * Sind die Werte ungültig, werden die Standardwerte
     * zurückgegeben.
     * Wenn max kleiner min ist, werden sie vertauscht,
     * damit das Bild nicht gespiegelt ist. */
    public Bereich leseBereich() {
        Bereich bereich;
        try {
            bereich = new Bereich(
                    Double.parseDouble(minREingabe.getText()),
                    Double.parseDouble(maxREingabe.getText()),
                    Double.parseDouble(minIEingabe.getText()),
                    Double.parseDouble(maxIEingabe.getText()));

            /* Wenn min und max vertauscht sind, anpassen.
             * Sonst ist das Bild spiegelverkehrt. */
            if(bereich.getKxMin() > bereich.getKxMax()) {
                bereich = new Bereich(bereich.getKxMax(), bereich.getKxMin(), bereich.getKyMin(), bereich.getKyMax());
            }
            if( bereich.getKyMin() > bereich.getKyMax()) {
                bereich = new Bereich(bereich.getKxMin(), bereich.getKxMax(), bereich.getKyMax(), bereich.getKyMin());
            }

        } catch(NumberFormatException | NullPointerException e) {
            System.out.println("Ungültige Eingaben! "+ e.getMessage());
            bereich = Standardwerte.standardGrenzen();
        }
        return bereich;
    }

    /** Liest die Eingabe im TextField und
     * gibt sie als int zurück.
     * Ist der Wert ungültig, wird der Standardwert
     * zurückgegeben. */
    public int leseMaxIterationen() {
        try {
            return Integer.parseInt(iterationenEingabe.getText());
        } catch(NumberFormatException e) {
            return Standardwerte.MAX_ITERATIONEN;
        }
    }

    /** Wird vom Reset-Button aufgerufen. */
    @FXML
    public void resetZeichenregion(ActionEvent event) {
        zeichenRegion.reset();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
