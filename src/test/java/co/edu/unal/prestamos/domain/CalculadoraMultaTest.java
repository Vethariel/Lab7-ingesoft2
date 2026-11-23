package co.edu.unal.prestamos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CalculadoraMultaTest {

    private static final LocalDate LIMITE = LocalDate.of(2026, 11, 25);

    // Arrange común: la calculadora no guarda estado, así que todas las pruebas comparten una
    private final CalculadoraMulta calculadora = new CalculadoraMulta();

    @Test
    void calcular_devueltoAntesDelLimite_noHayMulta() {
        // Act
        int multa = calculadora.calcular(LIMITE, LIMITE.minusDays(1), false);

        // Assert
        assertEquals(0, multa);
    }

    @Test
    void calcular_devueltoElDiaLimite_noHayMulta() {
        // Act
        int multa = calculadora.calcular(LIMITE, LIMITE, false);

        // Assert
        assertEquals(0, multa);
    }

    @Test
    void calcular_unDiaDeRetraso_cobraDosMil() {
        // Act
        int multa = calculadora.calcular(LIMITE, LIMITE.plusDays(1), false);

        // Assert
        assertEquals(2_000, multa);
    }
}
