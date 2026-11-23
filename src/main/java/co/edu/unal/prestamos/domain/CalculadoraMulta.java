package co.edu.unal.prestamos.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** Calcula la multa, en pesos, por devolver un equipo después de la fecha límite. */
public class CalculadoraMulta {

    static final int MULTA_POR_DIA = 2_000;

    public int calcular(LocalDate fechaLimite, LocalDate fechaDevolucion, boolean esMonitor) {
        long diasDeRetraso = ChronoUnit.DAYS.between(fechaLimite, fechaDevolucion);
        if (diasDeRetraso <= 0) {
            return 0;
        }
        return (int) Math.min(diasDeRetraso * MULTA_POR_DIA, 50000);
    }
}
