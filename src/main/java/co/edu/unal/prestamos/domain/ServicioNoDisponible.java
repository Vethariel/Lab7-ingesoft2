package co.edu.unal.prestamos.domain;

/**
 * Se lanza cuando un servicio externo detrás de un contrato del dominio no responde como
 * se espera (un código HTTP inesperado o un error de red). Vive en el dominio para que el
 * caso de uso y sus clientes puedan manejarlo sin depender de la capa data.
 */
public class ServicioNoDisponible extends RuntimeException {

    public ServicioNoDisponible(String mensaje) {
        super(mensaje);
    }

    public ServicioNoDisponible(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
