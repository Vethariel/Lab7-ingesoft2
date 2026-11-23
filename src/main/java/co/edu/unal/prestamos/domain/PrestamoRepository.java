package co.edu.unal.prestamos.domain;

/** Puerto de salida: guarda los préstamos y entrega sus identificadores. */
public interface PrestamoRepository {

    long siguienteId();

    void guardar(Prestamo prestamo);
}
