package co.edu.unal.prestamos.domain;

import java.time.LocalDate;

/**
 * Préstamo de un equipo a un estudiante. Es el agregado del dominio: sus operaciones
 * cambian el estado solo si se cumplen las reglas del negocio y, si no, lanzan un error.
 */
public class Prestamo {

    static final int DIAS_DE_PRESTAMO = 7;
    static final int DIAS_DE_RENOVACION = 7;
    static final int MAXIMO_DE_RENOVACIONES = 1;

    private final long id;
    private final String estudianteId;
    private final long equipoId;
    private EstadoPrestamo estado;
    private LocalDate fechaLimite;
    private int renovaciones;

    public Prestamo(long id, String estudianteId, long equipoId) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.equipoId = equipoId;
        this.estado = EstadoPrestamo.PENDIENTE;
        this.renovaciones = 0;
    }

    /** Aprueba un préstamo pendiente; la fecha límite queda a siete días de {@code hoy}. */
    public void aprobar(LocalDate hoy) {
        if (estado != EstadoPrestamo.PENDIENTE) {
            throw new IllegalStateException("Solo se aprueba un préstamo pendiente");
        }
        estado = EstadoPrestamo.APROBADO;
        fechaLimite = hoy.plusDays(DIAS_DE_PRESTAMO);
    }

    /** Extiende siete días la fecha límite de un préstamo aprobado. Solo se permite una vez. */
    public void renovar() {
        if (estado != EstadoPrestamo.APROBADO) {
            throw new IllegalStateException("Solo se renueva un préstamo aprobado");
        }
        if (renovaciones >= MAXIMO_DE_RENOVACIONES) {
            throw new IllegalStateException("El préstamo ya fue renovado");
        }
        fechaLimite = fechaLimite.plusDays(DIAS_DE_RENOVACION);
        renovaciones++;
    }

    /** Registra la devolución de un préstamo aprobado. */
    public void devolver() {
        if (estado != EstadoPrestamo.APROBADO) {
            throw new IllegalStateException("Solo se devuelve un préstamo aprobado");
        }
        estado = EstadoPrestamo.DEVUELTO;
    }

    public long getId() {
        return id;
    }

    public String getEstudianteId() {
        return estudianteId;
    }

    public long getEquipoId() {
        return equipoId;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    /** Fecha límite de devolución; es {@code null} mientras el préstamo no se aprueba. */
    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public int getRenovaciones() {
        return renovaciones;
    }
}
