package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.view;

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model.Bereich;

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.GlobalerThreadManager;
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.farbe.FarbAlgorithmus;
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.farbe.HsvFarbkreisLogarithmisch;
import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.rechnen.farbe.SchwarzWeis;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

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
 * werden hier damit versorgt. (GlobalerThreadManager).
 *
 * Geladen und zum Scene Graph hinzugefügt werden die
 * Elemente in {@link RootLayoutController}.
 *
 * TODO Die Grundfarbe auswählen können
 */
public class EingabenController implements Initializable {

    /** Die Inhalte der TextFields werden auf
     * die Standardwerte gesetzt. */
    private ParsingTextField minREingabe = new ParsingTextField<>(StandardwerteEingabe.MIN_R);
    private ParsingTextField maxREingabe = new ParsingTextField<>(StandardwerteEingabe.MAX_R);
    private ParsingTextField minIEingabe = new ParsingTextField<>(StandardwerteEingabe.MIN_I);
    private ParsingTextField maxIEingabe = new ParsingTextField<>(StandardwerteEingabe.MAX_I);

    private ParsingTextField iterationenEingabe = new ParsingTextField<>(StandardwerteEingabe.MAX_ITERATIONEN);
    private ParsingTextField distanzEingabe = new ParsingTextField<>(StandardwerteEingabe.MAX_DISTANZ);
    private ParsingTextField threadsEingabe = new ParsingTextField<>(StandardwerteEingabe.ANZAHL_THREADS);
    @FXML private ChoiceBox<FarbAlgorithmus> farbalgorithmusChoiceBox;

    private ParsingTextField breiteEingabe = new ParsingTextField<>(StandardwerteEingabe.SPEICHERN_BREITE);
    private ParsingTextField höheEingabe = new ParsingTextField<>(StandardwerteEingabe.SPEICHERN_HÖHE);

    /** Hier müssen die TextFields hinzugefügt werden. */
    @FXML private GridPane bereichGridPane;
    @FXML private GridPane parameterGridPane;
    @FXML private GridPane speichernGridPane;
    @FXML private TableView</*EintragV5*/Object> ansichtenTable;

    /** Muss gesetzt werden! */
    private RootLayoutController rootLayoutController;

    /** Die ParsingTextFields müssen manuel zur Scene hinzugefügt werden,
     * weil der SceneBuilder keine eigenen Klassen mag.
     * Außerdem ChangeListener für anzahlThreads und Tabelle initialisieren. */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bereichGridPane.add(minREingabe, 1, 0);
        bereichGridPane.add(maxREingabe, 1, 1);
        bereichGridPane.add(minIEingabe, 1, 2);
        bereichGridPane.add(maxIEingabe, 1, 3);
        parameterGridPane.add(iterationenEingabe, 1, 0);
        parameterGridPane.add(distanzEingabe, 1, 1);
        parameterGridPane.add(threadsEingabe, 1, 2);
        speichernGridPane.add(breiteEingabe, 1, 0);
        speichernGridPane.add(höheEingabe, 1, 1);

        farbalgorithmusChoiceBox.getItems().addAll(
                new HsvFarbkreisLogarithmisch(),
                new SchwarzWeis()
        );
        farbalgorithmusChoiceBox.getSelectionModel().select(0);


        // GlobalerThreadManager soll anzahlThreads wissen.
        threadsEingabe.textProperty().addListener(
            (observable, oldValue, newValue) -> {
                int anzahlThreads;
                try {
                    anzahlThreads = Integer.parseInt(newValue);
                } catch(NumberFormatException | NullPointerException e) {
                    System.out.println("Ungültige Eingaben! " + e.getMessage());
                    anzahlThreads = StandardwerteEingabe.ANZAHL_THREADS;
                }
                GlobalerThreadManager.Companion.setCorePoolSize(anzahlThreads);
            }
        );
/*
        //Schülerdaten in die Tabelle aufnehmen TODO
        vornameColumn.setCellValueFactory(cellData -> cellData.getValue().vornameProperty());
        nachnameColumn.setCellValueFactory(cellData -> cellData.getValue().nachnameProperty());

        // leer initialisieren
        showSchülerDetails(null);
        // ereignisbehandlung
        schülerTabableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSchülerDetails(newValue));*/
    }

    /** Speichert ein Bild der Zeichenfläche
     * mit der in den Eingaben spezifizierte Größe. */
    @FXML
    private void speichereBild(ActionEvent event) {
        rootLayoutController.getZeichenflächeController().speichereZeichenfläche();
    }

    //TODO Ansichten anzeigen
    //TODO Ansichten speichern mit Datei-Dialog
    //TODO Ansichten laden mit Datei-Dialog
    //TODO Bild speichern: Option Ansicht speichern

    public void setRootLayoutController(RootLayoutController rootLayoutController) {
        this.rootLayoutController = rootLayoutController;
    }

    //------------- Einlesen der Werte ------------//
    //
    // Ist der eingelesene Wert ungültig, wird der Standard-Wert verwendet.

    /** Setzt die angezeigten Werte in den Eingabefeldern
     * für minR, maxR, minI und maxI. */
    public void setzeBereichTexte(Bereich bereich) {
        minREingabe.setWert(bereich.getKxMin());
        maxREingabe.setWert(bereich.getKxMax());
        minIEingabe.setWert(bereich.getKyMin());
        maxIEingabe.setWert(bereich.getKyMax());
    }
    /** Liest die eingegebenen Werte in den Eingabefeldern
     * für minR, maxR, minI und maxI.
     * Ist nur einer der Werte ungültig, wird der
     * Standard-Bereich zurückgegeben. */
    public Bereich leseBereich() {
        Bereich bereich = new Bereich(
                (double) minREingabe.getWert(),
                (double) maxREingabe.getWert(),
                (double) minIEingabe.getWert(),
                (double) maxIEingabe.getWert());

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

    public void setzeMaxIterationenText(int maxIterationen) {
        iterationenEingabe.setWert(maxIterationen);
    }
    public int leseMaxIterationen() {
        return (int) iterationenEingabe.getWert();
    }

    public void setzeMaxDistanzText(double maxDistanz) {
        distanzEingabe.setWert(maxDistanz);
    }
    public double leseMaxDistanz() {
        return (double) distanzEingabe.getWert();
    }

    public void setzeAnzahlThreadsText(int anzahlThreads) {
        threadsEingabe.setWert(anzahlThreads);
    }
    public int leseAnzahlThreads() {
        return (int) threadsEingabe.getWert();
    }

    public void setzeFarbAlgorithmus(FarbAlgorithmus farbAlgorithmus) {
        farbalgorithmusChoiceBox.getSelectionModel().select(farbAlgorithmus);
    }
    public FarbAlgorithmus leseFarbAlgorithmus() {
        FarbAlgorithmus algorithmus = farbalgorithmusChoiceBox.getSelectionModel().getSelectedItem();
        if(algorithmus != null)
            return algorithmus;
        else
            return StandardwerteEingabe.FARB_ALGORITHMUS;
    }

    public void setzeBreiteText(double breite) {
        breiteEingabe.setWert(breite);
    }
    public double leseBreite() {
        return (double) breiteEingabe.getWert();
    }

    public void setzeHöheText(double höhe) {
        höheEingabe.setWert(höhe);
    }

    public double leseHöhe() {
        return (double) höheEingabe.getWert();
    }
}
