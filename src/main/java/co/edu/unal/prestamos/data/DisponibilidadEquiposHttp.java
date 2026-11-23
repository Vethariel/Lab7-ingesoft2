package co.edu.unal.prestamos.data;

import co.edu.unal.prestamos.domain.DisponibilidadEquipos;
import co.edu.unal.prestamos.domain.ServicioNoDisponible;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Adaptador que cumple {@link DisponibilidadEquipos} consultando la API de equipos:
 * {@code GET <urlBase>/api/v1/equipos/<id>}.
 */
public class DisponibilidadEquiposHttp implements DisponibilidadEquipos {

    private static final String RUTA_EQUIPOS = "/api/v1/equipos/";
    private static final String ESTADO_DISPONIBLE = "disponible";
    private static final int HTTP_OK = 200;
    private static final int HTTP_NOT_FOUND = 404;
    private static final Duration TIEMPO_MAXIMO = Duration.ofSeconds(5);

    private final String urlBase;
    private final HttpClient cliente;
    private final Gson gson = new Gson();

    public DisponibilidadEquiposHttp(String urlBase) {
        this.urlBase = urlBase.endsWith("/") ? urlBase.substring(0, urlBase.length() - 1) : urlBase;
        this.cliente = HttpClient.newBuilder().connectTimeout(TIEMPO_MAXIMO).build();
    }

    @Override
    public boolean estaDisponible(long equipoId) {
        HttpRequest peticion = HttpRequest.newBuilder(URI.create(urlBase + RUTA_EQUIPOS + equipoId))
                .header("Accept", "application/json")
                .timeout(TIEMPO_MAXIMO)
                .GET()
                .build();

        HttpResponse<String> respuesta = enviar(peticion);

        if (respuesta.statusCode() == HTTP_OK) {
            EquipoJson equipo = gson.fromJson(respuesta.body(), EquipoJson.class);
            return ESTADO_DISPONIBLE.equals(equipo.estado);
        }
        if (respuesta.statusCode() == HTTP_NOT_FOUND) {
            return false;
        }
        throw new ServicioNoDisponible("HTTP " + respuesta.statusCode());
    }

    private HttpResponse<String> enviar(HttpRequest peticion) {
        try {
            return cliente.send(peticion, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            String mensaje = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            throw new ServicioNoDisponible(mensaje, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServicioNoDisponible("Consulta interrumpida", e);
        }
    }

    /** Forma del JSON que devuelve la API: {"id": 12, "nombre": "Portatil", "estado": "disponible"}. */
    private static class EquipoJson {
        long id;
        String nombre;
        String estado;
    }
}
