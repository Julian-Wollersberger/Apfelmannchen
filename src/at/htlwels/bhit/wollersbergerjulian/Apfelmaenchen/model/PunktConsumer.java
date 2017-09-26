package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.model;

// Created by julian on 27.06.17.
/**
 * Diese Klasse soll Lambda expressions ermöglichen.
 * Siehe {@link java.util.function.Consumer und die Implementierung von {@link java.util.ArrayList#forEach}.
 */
public interface PunktConsumer {

    public void accept(double cr, double ci, int iterationen);
}
