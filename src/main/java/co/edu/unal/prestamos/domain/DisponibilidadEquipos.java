package co.edu.unal.prestamos.domain;

/** Puerto de salida: consulta si un equipo se puede prestar. La capa data lo implementa. */
public interface DisponibilidadEquipos {

    boolean estaDisponible(long equipoId);
}
