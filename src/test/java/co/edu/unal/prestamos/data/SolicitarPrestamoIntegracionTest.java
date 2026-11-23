package co.edu.unal.prestamos.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import co.edu.unal.prestamos.domain.EstadoPrestamo;
import co.edu.unal.prestamos.domain.Notificador;
import co.edu.unal.prestamos.domain.Prestamo;
import co.edu.unal.prestamos.domain.SolicitarPrestamo;
import java.io.IOException;
import java.util.List;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Extremo a extremo: el caso de uso real con el adaptador HTTP real contra el servidor de
 * prueba, el repositorio fake en memoria y un notificador mock.
 */
class SolicitarPrestamoIntegracionTest {

    private MockWebServer servidor;

    @BeforeEach
    void iniciarServidor() throws IOException {
        servidor = new MockWebServer();
        servidor.start();
    }

    @AfterEach
    void detenerServidor() {
        servidor.close();
    }

    @Test
    void ejecutar_equipoDisponibleEnLaApi_guardaElPrestamoEnElRepositorio() {
        // Arrange
        servidor.enqueue(new MockResponse.Builder()
                .code(200)
                .addHeader("Content-Type", "application/json")
                .body("{\"id\": 12, \"nombre\": \"Portatil\", \"estado\": \"disponible\"}")
                .build());
        DisponibilidadEquiposHttp disponibilidad = new DisponibilidadEquiposHttp(servidor.url("/").toString());
        PrestamoRepositoryEnMemoria repositorio = new PrestamoRepositoryEnMemoria();
        Notificador notificador = mock(Notificador.class);
        SolicitarPrestamo casoDeUso = new SolicitarPrestamo(disponibilidad, repositorio, notificador);

        // Act
        Prestamo p = casoDeUso.ejecutar("1023", 12);

        // Assert
        List<Prestamo> guardados = repositorio.todos();
        assertEquals(1, guardados.size());
        assertEquals(p, guardados.get(0));
        assertEquals(1, p.getId());
        assertEquals("1023", p.getEstudianteId());
        assertEquals(12, p.getEquipoId());
        assertEquals(EstadoPrestamo.PENDIENTE, p.getEstado());
        verify(notificador).notificar("1023", "Solicitud 1 registrada");
    }
}
