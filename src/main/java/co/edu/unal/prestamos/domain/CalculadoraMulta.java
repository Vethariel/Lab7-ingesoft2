package co.edu.unal.prestamos.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** Calcula la multa, en pesos, por devolver un equipo después de la fecha límite. */
public class CalculadoraMulta {

    static final int SIN_MULTA = 0;
    static final int MULTA_POR_DIA = 2_000;
    static final int MULTA_MAXIMA = 50_000;
    /** Los monitores pagan la mitad: la multa se divide entre este valor, después del tope. */
    static final int DIVISOR_MONITOR = 2;

    public int calcular(LocalDate fechaLimite, LocalDate fechaDevolucion, boolean esMonitor) {
        long diasDeRetraso = ChronoUnit.DAYS.between(fechaLimite, fechaDevolucion);
        if (diasDeRetraso <= 0) {
            return SIN_MULTA;
        }
        int multa = multaConTope(diasDeRetraso);
        return esMonitor ? multa / DIVISOR_MONITOR : multa;
    }

    private int multaConTope(long diasDeRetraso) {
        return (int) Math.min(diasDeRetraso * MULTA_POR_DIA, MULTA_MAXIMA);
    }
}
