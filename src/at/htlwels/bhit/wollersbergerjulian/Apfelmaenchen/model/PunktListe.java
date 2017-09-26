package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.model;

// Created by julian on 27.06.17.
/**
 * Diese Klasse enthält eine Liste von Punkten.
 * Bzw drei Arrays (für alle crListe, ciListe und iterationenListe);
 * damit wird nicht für jeden Punkt ein neues Objekt angelegt,
 * was Rechenzeit sparen soll.
 */
public class PunktListe {

    // Die Arrays für die Punkte.
    // eigentlich könnte ich mir die crListe sparen, wenn immer eine Spalte verwendet wird.
    private double[] crListe;
    private double[] ciListe;
    private int[] iterationenListe;

    // Zeiger auf das letzte hinzugefügte Element
    private int currentPosition;

    /** Konstruktor mit Werten.
     * Die Arrays müssen die gleiche Länge haben. */
    public PunktListe(double[] crListe, double[] ciListe, int[] iterationenListe, int currentPosition) {
        if(crListe.length != ciListe.length || ciListe.length != iterationenListe.length)
            throw new IllegalArgumentException("Arrays sind unterschiedlicher Länge!");
        else {
            this.crListe = crListe;
            this.ciListe = ciListe;
            this.iterationenListe = iterationenListe;
            this.currentPosition = currentPosition;
        }
    }

    /** Erzeugt die Arrays mit der angegebenen Größe
     * und setzt die Position auf 0. */
    public PunktListe(int größe) {
        this.crListe = new double[größe];
        this.ciListe = new double[größe];
        this.iterationenListe = new int[größe];
        this.currentPosition = 0;
        //System.out.println(größe);
    }

    /** Fügt ein Element zur Liste hinzu.
     * @throws IndexOutOfBoundsException Wenn die Arraygröße üerschritten wird */
    public void add(double cr, double ci, int iterationen) throws IndexOutOfBoundsException {
        crListe[currentPosition] = cr;
        ciListe[currentPosition] = ci;
        iterationenListe[currentPosition] = iterationen;
        currentPosition++;
    }

    /**Diese Funktion nimmt eine Lambda Expression als Parameter.
     * Mit ihr wird eine Operation für alle Paare von Werten ausgeführt.
     *
     * Bsp: this.forEach(cr, ci, iterationen -> {sout(cr +" "+ ci +" "+iterationen})
     *
     * Siehe {@link java.util.function.Consumer und die Implementierung von {@link java.util.ArrayList#forEach}. */
    public void forEach(PunktConsumer action) {
        for (int i=0; i < crListe.length; i++) {
            action.accept(crListe[i], ciListe[i], iterationenListe[i]);
        }
    }

    // Element löschen ? Wäre dann ArrayList.
    // beliebiges bearbeiten ?
}
