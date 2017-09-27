package at.htlwels.bhit.wollersbergerjulian.apfelmännchen.model

// Created by julian on 30.07.17.
/**
 * Diese Klasse repräsentiert ein Rechteck, das
 * mit zwei Koordinatensystemen beschrieben wird.
 * 
 * Die beiden Koordinatensysteme haben unterschiedliche
 * Skalierungen und Ursprünge. Mit dieser Klasse können
 * Koordinaten zwischen diesen beiden Systemen leicht
 * umgerechnet werden und die Umrechnung ist
 * in beide Richtungen eindeutig.
 * 
 * Außerdem beinhaltet diese Klasse Grenzen
 * für einen rechteckigen Bereich, der eben
 * in zwei 2D-Koordinatensystemen beschrieben wird.
 * 
 * Der Bereich darf verändert werden, aber
 * jede Funktion, die die Koordsys. gegeneinander verschiebt,
 * muss ein neues Objekt zurückgeben, anstatt das
 * vorhandene zu ändern. 
 * 
 * # Bezeichnungen
 * + Pixel-Koordsys (w|h): in 1px
 *      Minimale Breite und Höhe sind 0.
 *      breite (geht nach rechts).
 *      höhe geht nach unten!
 *      Andere Werte heißen breite und höhe.
 * + Kartesisches Koordsys (kx|ky): in 1
 *      kxMin und kyMin != 0.
 *      kxMax.
 *      kyMax geht nach oben.
 *      Andere Werte heißen kx und ky
 *
 *  scaleKX und scaleKY sind der Skalierungsfaktor.
 *  Weil höhe in die andere Richtung geht als ky,
 *  (und damit scaleHöhe nicht negativ ist, )
 *  sind in einigen Formeln ein Vorzeichen vertauscht.
 *  Vb und Vh sind hier der Versatz der Koordinaten-Ursprünge in Pixel.
 *  Vh geht wie höhe nach unten.
 */
data class DoppelKoordinatenSystem(
    /* Kartesischer Bereich.
     * Es wird nur dieser gespeichert und 
     * der Pixel-Bereich wird immer ausgerechnet. */
    var kxMin: Double,
    var kxMax: Double,
    var kyMin: Double,
    var kyMax: Double,

    /** Skalierunsfaktor in px/1.
     * Wenn diese gleich sind, gibt es keine Verzerrung. */
    var scaleBreite: Double,
    var scaleHöhe: Double,

    /** Verschiebung der Koordinaten-Ursprünge in px */
    var Vb: Double,
    var Vh: Double
) {
    /** Breite wird aus den anderen Werten berechnet.*/
    var breite: Double
    get() = kxToBreite(kxMax)
    /** Verändert nur kxMax */
    set(breite) {kxMax = breiteToKX(breite)}

    var höhe: Double
    /** Weil Achsen entgegengesetzt sind, entspricht kxMin der höhe. */
    get() = kyToHöhe(kyMin)
    /** Verändert nur kyMax */
    set(höhe) {kyMax = höheToKY(höhe)}

    /** Der Kartesische Bereich als Bereich-Objekt. */
    var kBereich: Bereich
    get() = Bereich(kxMin, kxMax, kyMin, kyMax)
    set(value) {kxMin=value.kxMin; kxMax=value.kxMax; kyMin=value.kyMin; kyMax=value.kyMax}

    /** Spanne; Ende minus Anfang. */
    val kxSpanne get() = (kxMax - kxMin)
    val kySpanne get() = (kyMax - kyMin)

    /** Konstruktor mit kartesischem Bereich, breite und höhe.
     * scale und V werden daraus ausgerechnet. */
    constructor(kBereich: Bereich, breite: Double, höhe: Double) 
            : this(kBereich.kxMin, kBereich.kxMax, kBereich.kyMin, kBereich.kyMax, 
                berechneScaleBreite(breite, kBereich.kxSpanne), berechneScaleHöhe(höhe, kBereich.kySpanne), 
                berechneVb(breite, kBereich), berechneVh(höhe, kBereich))

    /**Wandelt einen kartesischen Wert in einen Pixel-Wert.
     * @param kx Der kx-Wert des kartesischen Koordinatensystems.
     * @return Den Pixel-Abstand vom linken Rand des Rechecks. */
    fun kxToBreite(kx: Double): Double { return Vb + kx*scaleBreite }
    fun kyToHöhe(ky: Double): Double { return Vh - ky*scaleHöhe }
    fun breiteToKX(w: Double): Double { return  (w - Vb) / scaleBreite }
    fun höheToKY(h: Double): Double { return -(h - Vh) / scaleHöhe }

    /** Statische Methoden zum Berechnen von scale und V. */
    companion object {
        fun berechneScaleBreite(breite: Double, kxSpanne: Double): Double 
        {return breite / kxSpanne}
        fun berechneScaleHöhe(höhe: Double, kySpanne: Double): Double
        {return höhe / kySpanne}
        
        fun berechneVb(breite: Double, kBereich: Bereich): Double 
        {return -(breite * kBereich.kxMin) / kBereich.kxSpanne}
        fun berechneVh(höhe: Double, kBereich: Bereich): Double
        {return +(höhe * kBereich.kyMax) / kBereich.kySpanne}
    }
    
    /** Bereich und scale anpassen, sodass es nicht mehr verzerrt ist.
     * Das Kartesische Koordsys braucht ein Verhältnis von 1:1. */
    fun entzerre(): DoppelKoordinatenSystem {
        if(scaleBreite > scaleHöhe) {
            // Ermitteln, wie viel links und rechts hinzugefügt werden muss.
            val rand = (breite - kxSpanne*scaleHöhe) /scaleHöhe /2
            /* Bereich anpassen: Rand hinzufügen.
             * scales sind jetzt gleich, V muss neu berechnet werden. */
            return DoppelKoordinatenSystem(Bereich(kxMin - rand, kxMax + rand,
                    kyMin, kyMax), breite, höhe)
        } else {
            // Analog, nur Bereich oben und unten erweitern.
            val rand = (höhe - kySpanne*scaleBreite) /scaleBreite /2
            return DoppelKoordinatenSystem(Bereich(kxMin, kxMax,
                    kyMin - rand, kyMax + rand), breite, höhe)
        }
    }

    /**Erzeugt einen Ausschnitt aus diesem Koordinatensystem.
     * Der erzeugt Bereich ist ein gezoomter Ausschnitt, sodass
     * das Zoom-Zentrum (breite|höhe) des alten und neuen Koordsys
     * das selbe sind.
     *
     * # Eigenschaften:
     * + Der kartesische Punkt, über dem die Maus ist,
     *   soll vorher und nachher der selbe sein.
     * + breite und höhe verändern sich nicht.
     * + zoomfaktor==2 heißt, dass die Spanne halbiert wird.
     *
     * + scale /= faktor
     * + Die neue Spanne ist Spanne/zoomFaktor.
     * + Alle anderen Vorher-/Nachher-Maße können nicht multipliziert werden.
     * */
    fun erzeugeKartesischenAusschnitt(zoomFaktor: Double, zentrumBreite: Double, zentrumHöhe: Double): DoppelKoordinatenSystem {
        /* Wo die Maus ist, ist das Zentrum der Transformation.
         * Diese Koordinaten sollen genau dort bleiben, wo sie sind. */
        val zentrumKx = breiteToKX(zentrumBreite)
        val zentrumKy = höheToKY(zentrumHöhe)

        /* Zuerst skalieren:
         * Neues Koordsys aus (skallierterBereich, alteBreite, alteHöhe).
         * Resultat: Bereich *= faktor; scale /= faktor; V bleibt gleich. */
        val skalKoords = DoppelKoordinatenSystem(
                kBereich.skalliert(zoomFaktor),
                breite, höhe)

        /* Um so viel, wie das Zentrum verschoben wurde, muss der Bereich
        * mitverschoben werden. */
        val deltaKX = (skalKoords.kxToBreite(zentrumKx) - zentrumBreite) /skalKoords.scaleBreite
        val deltaKY = -(skalKoords.kyToHöhe(zentrumKy) - zentrumHöhe) /skalKoords.scaleHöhe

        /* Dann Bereich verschieben: */
        return DoppelKoordinatenSystem(skalKoords.kBereich.verschoben(deltaKX, deltaKY),
                breite, höhe)
    }

    /** Verschiebung in layoutChildren berechnen.
     * Nur dieses ist das, was angezeigt wird.
     * Scale und breite vom ImageKoordsys sind falsch.
     * verschiebung(Bereich imageBereich)
     *
     * Bzw. anderes Koordsys mit Koordinaten von diesem hier ausdrücken.
     * */
}