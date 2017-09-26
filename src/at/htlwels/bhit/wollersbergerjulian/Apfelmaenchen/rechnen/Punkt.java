package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.rechnen;

/** Eine Struktur, die die drei Werte eines zu zeichnenden Punktes enthält.
    * Vielleicht ohne Klasse, dafür alle Werte einzeln drauflegen, wie es
    * der gc macht? */
public class Punkt {
    public double cr;
    public double ci;
    public int iterationen;

    public Punkt(double cr, double ci, int iterationen) {
        this.cr = cr;
        this.ci = ci;
        this.iterationen = iterationen;
    }
}
