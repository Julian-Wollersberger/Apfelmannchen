package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.model;

// Created by julian on 11.07.17.
/**
 * Diese Klasse enthält die Grenzen eines
 * {@link at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.anzeigen.KomplexesCanvas}:
 * <br>minR, maxR, minI, maxI,
 */
public class Bereich {

    /* Der Bereich, in dem die komlexen Werte sind. */
    private double minR;
    private double maxR;
    private double minI;
    private double maxI;

    public Bereich(double minR, double maxR, double minI, double maxI) {
        this.minR = minR;
        this.maxR = maxR;
        this.minI = minI;
        this.maxI = maxI;
    }

    @Override
    public String toString() {
        return "Bereich{" +
                "minR=" + minR +
                ", maxR=" + maxR +
                ", minI=" + minI +
                ", maxI=" + maxI +
                '}';
    }

    public double getMinR() {
        return minR;
    }
    public void setMinR(double minR) {
        this.minR = minR;
    }
    public double getMaxR() {
        return maxR;
    }
    public void setMaxR(double maxR) {
        this.maxR = maxR;
    }
    public double getMinI() {
        return minI;
    }
    public void setMinI(double minI) {
        this.minI = minI;
    }
    public double getMaxI() {
        return maxI;
    }
    public void setMaxI(double maxI) {
        this.maxI = maxI;
    }

}
