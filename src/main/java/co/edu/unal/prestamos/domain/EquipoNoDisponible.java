package co.edu.unal.prestamos.domain;

/** Se lanza cuando se pide un equipo que no se puede prestar. */
public class EquipoNoDisponible extends RuntimeException {

    public EquipoNoDisponible(long equipoId) {
        super("Equipo " + equipoId + " no disponible");
    }
}
