package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view;

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.Bereich;

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.ThreadManager;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

// Created by julian on 25.08.17.
/**
 * Der EingabenController verwaltet den Teil des
 * GUIs, der für Eingaben des Benutzers zuständig ist.
 *
 * Die Hauptaufgabe dieses Controllers ist es, in die
 * Eingabefelder Werte zu schreiben und auszulesen.
 *
 * Statische Klassen, die Eingabe-Werte brauchen,
 * werden hier damit versorgt. (ThreadManager).
 *
 * Geladen und zum Scene Graph hinzugefügt werden die
 * Elemente in {@link RootLayoutController}.
 *
 * TODO Die Grundfarbe auswählen können
 */
public class EingabenController implements Initializable {

    @FXML private TextField minREingabe;
    @FXML private TextField maxREingabe;
    @FXML private TextField minIEingabe;
    @FXML private TextField maxIEingabe;

    @FXML private TextField iterationenEingabe;
    @FXML private TextField threadsEingabe;
    @FXML private TextField distanzEingabe;

    @FXML private TextField breiteEingabe;
    @FXML private TextField höheEingabe;

    /** Muss gesetzt werden! */
    private RootLayoutController rootLayoutController;

    /** Die Inhalte der TextFields werden auf
     * die Standardwerte gesetzt. */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setzeBereichTexte(StandardwerteEingabe.standardBereich());
        setzeMaxIterationenText(StandardwerteEingabe.MAX_ITERATIONEN);
        setzeMaxDistanzText(StandardwerteEingabe.MAX_DISTANZ);
        setzeAnzahlThreadsText(StandardwerteEingabe.ANZAHL_THREADS);
        setzeBreiteText(StandardwerteEingabe.SPEICHERN_BREITE);
        setzeHöheText(StandardwerteEingabe.SPEICHERN_HÖHE);

        // ThreadManager soll anzahlThreads wissen.
        threadsEingabe.textProperty().addListener(
            (observable, oldValue, newValue) -> {
                int anzahlThreads;
                try {
                    anzahlThreads = Integer.parseInt(newValue);
                } catch(NumberFormatException | NullPointerException e) {
                    System.out.println("Ungültige Eingaben! " + e.getMessage());
                    anzahlThreads = StandardwerteEingabe.ANZAHL_THREADS;
                }
                ThreadManager.Companion.setCorePoolSize(anzahlThreads);
            }
        );
    }

    /** Speichert ein Bild der Zeichenfläche
     * mit der in den Eingaben spezifizierte Größe. */
    @FXML
    private void speichereBild(ActionEvent event) {
        rootLayoutController.getZeichenflächeController().speichereZeichenfläche(true);
    }

    /** Setzt die angezeigten Werte in den Eingabefeldern
     * für minR, maxR, minI und maxI. */
    public void setzeBereichTexte(Bereich bereich) {
        minREingabe.setText(Double.toString(bereich.getKxMin()));
        maxREingabe.setText(Double.toString(bereich.getKxMax()));
        minIEingabe.setText(Double.toString(bereich.getKyMin()));
        maxIEingabe.setText(Double.toString(bereich.getKyMax()));
    }
    /** Liest die eingegebenen Werte in den Eingabefeldern
     * für minR, maxR, minI und maxI.
     * Ist nur einer der Werte ungültig, wird der
     * Standard-Bereich zurückgegeben. */
    public Bereich leseBereich() {
        Bereich bereich;
        try {
            bereich = new Bereich(
                    Double.parseDouble(minREingabe.getText()),
                    Double.parseDouble(maxREingabe.getText()),
                    Double.parseDouble(minIEingabe.getText()),
                    Double.parseDouble(maxIEingabe.getText()));

        } catch(NumberFormatException | NullPointerException e) {
            System.out.println("Ungültige Eingaben! "+ e.getMessage());
            bereich = StandardwerteEingabe.standardBereich();
        }

        /* Wenn min und max vertauscht sind, anpassen.
         * Sonst ist das Bild spiegelverkehrt. */
        if(bereich.getKxMin() > bereich.getKxMax()) {
            bereich = new Bereich(bereich.getKxMax(), bereich.getKxMin(), bereich.getKyMin(), bereich.getKyMax());
        }
        if( bereich.getKyMin() > bereich.getKyMax()) {
            bereich = new Bereich(bereich.getKxMin(), bereich.getKxMax(), bereich.getKyMax(), bereich.getKyMin());
        }

        return bereich;
    }

    /** Setzt den angezeigten Wert im Eingabefeld für
     * die Maximale-Iterationen-Anzahl. */
    public void setzeMaxIterationenText(int maxIterationen) {
        iterationenEingabe.setText(Integer.toString(maxIterationen));
    }
    /** Liest die Eingabe im TextField und
     * gibt sie als int zurück.
     * Ist der Wert ungültig, wird der Standardwert
     * zurückgegeben. */
    public int leseMaxIterationen() {
        try {
            return Integer.parseInt(iterationenEingabe.getText());
        } catch(NumberFormatException e) {
            System.out.println("Ungültige Eingabe! "+ e.getMessage());
            return StandardwerteEingabe.MAX_ITERATIONEN;
        }
    }

    /** Setzt den angezeigten Wert im Eingabefeld für
     * die maximale Distanz. */
    public void setzeMaxDistanzText(double maxDistanz) {
        distanzEingabe.setText(Double.toString(maxDistanz));
    }
    /** Liest die Eingabe im TextField und
     * gibt sie als double zurück.
     * Ist der Wert ungültig, wird der Standardwert
     * zurückgegeben. */
    public double leseMaxDistanz() {
        try {
            return Double.parseDouble(distanzEingabe.getText());
        } catch(NumberFormatException e) {
            System.out.println("Ungültige Eingabe! "+ e.getMessage());
            return StandardwerteEingabe.MAX_DISTANZ;
        }
    }

    /** Setzt den angezeigten Wert im Eingabefeld für
     * die Thread-Anzahl. */
    public void setzeAnzahlThreadsText(int anzahlThreads) {
        threadsEingabe.setText(Integer.toString(anzahlThreads));
    }
    /** Liest die Eingabe im TextField und
     * gibt sie als int zurück.
     * Ist der Wert ungültig, wird der Standardwert
     * zurückgegeben. */
    public int leseAnzahlThreads() {
        try {
            return Integer.parseInt(threadsEingabe.getText());
        } catch(NumberFormatException e) {
            System.out.println("Ungültige Eingabe! "+ e.getMessage());
            return StandardwerteEingabe.ANZAHL_THREADS;
        }
    }

    /** Setzt den angezeigten Wert im Eingabefeld für
     * die Breite beim Speichern. */
    public void setzeBreiteText(double breite) {
        breiteEingabe.setText(Double.toString(breite));
    }
    /** Liest die Eingabe im TextField und
     * gibt sie als int zurück.
     * Ist der Wert ungültig, wird der Standardwert
     * zurückgegeben. */
    public double leseBreite() {
        try {
            return Double.parseDouble(breiteEingabe.getText());
        } catch(NumberFormatException e) {
            System.out.println("Ungültige Eingabe! "+ e.getMessage());
            return StandardwerteEingabe.SPEICHERN_BREITE;
        }
    }

    /** Setzt den angezeigten Wert im Eingabefeld für
     * die Breite beim Speichern. */
    public void setzeHöheText(double höhe) {
        höheEingabe.setText(Double.toString(höhe));
    }
    /** Liest die Eingabe im TextField und
     * gibt sie als int zurück.
     * Ist der Wert ungültig, wird der Standardwert
     * zurückgegeben. */
    public double leseHöhe() {
        try {
            return Double.parseDouble(höheEingabe.getText());
        } catch(NumberFormatException e) {
            System.out.println("Ungültige Eingabe! "+ e.getMessage());
            return StandardwerteEingabe.SPEICHERN_HÖHE;
        }
    }

    public void setRootLayoutController(RootLayoutController rootLayoutController) {
        this.rootLayoutController = rootLayoutController;
    }
}
