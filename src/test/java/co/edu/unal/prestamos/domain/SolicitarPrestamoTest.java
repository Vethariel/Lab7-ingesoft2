package co.edu.unal.prestamos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SolicitarPrestamoTest {

    private DisponibilidadEquipos disponibilidad;
    private PrestamoRepository repositorio;
    private Notificador notificador;
    private SolicitarPrestamo casoDeUso;

    @BeforeEach
    void crearDobles() {
        // Arrange común: tres mocks y el caso de uso que los recibe por constructor
        disponibilidad = mock(DisponibilidadEquipos.class);
        repositorio = mock(PrestamoRepository.class);
        notificador = mock(Notificador.class);
        casoDeUso = new SolicitarPrestamo(disponibilidad, repositorio, notificador);
    }

    @Test
    void ejecutar_equipoDisponible_guardaYNotifica() {
        // Arrange
        when(disponibilidad.estaDisponible(12)).thenReturn(true);
        when(repositorio.siguienteId()).thenReturn(50L);

        // Act
        Prestamo p = casoDeUso.ejecutar("1023", 12);

        // Assert
        assertEquals(EstadoPrestamo.PENDIENTE, p.getEstado());
        verify(repositorio, times(1)).guardar(p);
        verify(notificador).notificar("1023", "Solicitud 50 registrada");
    }

    @Test
    void ejecutar_equipoNoDisponible_noGuardaNiNotifica() {
        // Arrange
        when(disponibilidad.estaDisponible(12)).thenReturn(false);

        // Act
        EquipoNoDisponible error = assertThrows(EquipoNoDisponible.class,
                () -> casoDeUso.ejecutar("1023", 12));

        // Assert
        assertEquals("Equipo 12 no disponible", error.getMessage());
        verify(repositorio, never()).guardar(any());
        verifyNoInteractions(notificador);
    }

    @Test
    void ejecutar_falloAlGuardar_noNotifica() {
        // Arrange
        when(disponibilidad.estaDisponible(12)).thenReturn(true);
        when(repositorio.siguienteId()).thenReturn(50L);
        doThrow(new RuntimeException("BD caída")).when(repositorio).guardar(any());

        // Act
        RuntimeException error = assertThrows(RuntimeException.class,
                () -> casoDeUso.ejecutar("1023", 12));

        // Assert
        assertEquals("BD caída", error.getMessage());
        verifyNoInteractions(notificador);
    }
}
