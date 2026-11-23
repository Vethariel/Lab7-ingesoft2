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

    @Test
    void calcular_veinticincoDiasDeRetraso_cobraJustoElTope() {
        // Act
        int multa = calculadora.calcular(LIMITE, LIMITE.plusDays(25), false);

        // Assert
        assertEquals(50_000, multa);
    }

    @Test
    void calcular_cuarentaDiasDeRetraso_noPasaDelTope() {
        // Act
        int multa = calculadora.calcular(LIMITE, LIMITE.plusDays(40), false);

        // Assert
        assertEquals(50_000, multa);
    }

    @Test
    void calcular_monitorConTresDiasDeRetraso_pagaLaMitad() {
        // Act
        int multa = calculadora.calcular(LIMITE, LIMITE.plusDays(3), true);

        // Assert
        assertEquals(3_000, multa);
    }

    @Test
    void calcular_monitorConCuarentaDiasDeRetraso_pagaLaMitadDelTope() {
        // Act
        int multa = calculadora.calcular(LIMITE, LIMITE.plusDays(40), true);

        // Assert
        assertEquals(25_000, multa);
    }
}
