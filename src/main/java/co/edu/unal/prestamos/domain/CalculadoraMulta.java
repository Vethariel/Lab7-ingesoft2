package co.edu.unal.prestamos.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculadoraMulta {

    public int calcular(LocalDate fechaLimite, LocalDate fechaDevolucion, boolean esMonitor) {
        long diasDeRetraso = ChronoUnit.DAYS.between(fechaLimite, fechaDevolucion);
        if (diasDeRetraso <= 0) {
            return 0;
        }
        return (int) diasDeRetraso * 2000;
    }
}
