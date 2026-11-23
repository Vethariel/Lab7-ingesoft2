package co.edu.unal.prestamos.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.unal.prestamos.domain.ServicioNoDisponible;
import java.io.IOException;
import java.net.ConnectException;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import mockwebserver3.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** El adaptador real, con su HttpClient y Gson, contra un servidor HTTP local (MockWebServer). */
class DisponibilidadEquiposHttpTest {

    private MockWebServer servidor;
    private DisponibilidadEquiposHttp adaptador;

    @BeforeEach
    void iniciarServidor() throws IOException {
        servidor = new MockWebServer();
        servidor.start();
        adaptador = new DisponibilidadEquiposHttp(servidor.url("/").toString());
    }

    @AfterEach
    void detenerServidor() {
        servidor.close();
    }

    private void responder(int codigo, String cuerpo) {
        servidor.enqueue(new MockResponse.Builder()
                .code(codigo)
                .addHeader("Content-Type", "application/json")
                .body(cuerpo)
                .build());
    }

    @Test
    void estaDisponible_equipoDisponible_devuelveVerdaderoYLlamaLaRutaCorrecta() throws InterruptedException {
        // Arrange
        responder(200, "{\"id\": 12, \"nombre\": \"Portatil\", \"estado\": \"disponible\"}");

        // Act
        boolean disponible = adaptador.estaDisponible(12);

        // Assert
        assertTrue(disponible);
        RecordedRequest peticion = servidor.takeRequest();
        assertEquals("GET", peticion.getMethod());
        assertEquals("/api/v1/equipos/12", peticion.getUrl().encodedPath());
    }

    @Test
    void estaDisponible_equipoPrestado_devuelveFalso() {
        // Arrange
        responder(200, "{\"id\": 12, \"nombre\": \"Portatil\", \"estado\": \"prestado\"}");

        // Act
        boolean disponible = adaptador.estaDisponible(12);

        // Assert
        assertFalse(disponible);
    }

    @Test
    void estaDisponible_equipoInexistente404_devuelveFalso() {
        // Arrange
        responder(404, "{\"error\": \"Equipo no encontrado\"}");

        // Act
        boolean disponible = adaptador.estaDisponible(99);

        // Assert
        assertFalse(disponible);
    }

    @Test
    void estaDisponible_errorDelServidor500_lanzaServicioNoDisponible() {
        // Arrange
        responder(500, "{\"error\": \"Error interno\"}");

        // Act
        ServicioNoDisponible error = assertThrows(ServicioNoDisponible.class,
                () -> adaptador.estaDisponible(12));

        // Assert
        assertEquals("HTTP 500", error.getMessage());
    }

    @Test
    void estaDisponible_servidorApagado_lanzaServicioNoDisponibleConElErrorDeRed() {
        // Arrange: el servidor se apaga, así que la conexión es rechazada
        servidor.close();

        // Act
        ServicioNoDisponible error = assertThrows(ServicioNoDisponible.class,
                () -> adaptador.estaDisponible(12));

        // Assert
        assertInstanceOf(ConnectException.class, error.getCause());
    }
}
