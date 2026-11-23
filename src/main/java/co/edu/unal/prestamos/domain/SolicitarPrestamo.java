package co.edu.unal.prestamos.domain;

/**
 * Caso de uso: un estudiante solicita un equipo. Depende solo de los tres contratos del
 * dominio, que recibe por constructor; no sabe si detrás hay HTTP, una base de datos o un mock.
 */
public class SolicitarPrestamo {

    private final DisponibilidadEquipos disponibilidad;
    private final PrestamoRepository repositorio;
    private final Notificador notificador;

    public SolicitarPrestamo(DisponibilidadEquipos disponibilidad, PrestamoRepository repositorio,
            Notificador notificador) {
        this.disponibilidad = disponibilidad;
        this.repositorio = repositorio;
        this.notificador = notificador;
    }

    public Prestamo ejecutar(String estudianteId, long equipoId) {
        if (!disponibilidad.estaDisponible(equipoId)) {
            throw new EquipoNoDisponible(equipoId);
        }
        Prestamo prestamo = new Prestamo(repositorio.siguienteId(), estudianteId, equipoId);
        repositorio.guardar(prestamo);
        notificador.notificar(estudianteId, "Solicitud " + prestamo.getId() + " registrada");
        return prestamo;
    }
}
