package co.edu.unal.prestamos.data;

import co.edu.unal.prestamos.domain.Prestamo;
import co.edu.unal.prestamos.domain.PrestamoRepository;
import java.util.ArrayList;
import java.util.List;

/** Fake de PrestamoRepository: una lista y un contador. Funciona de verdad, pero en memoria. */
class PrestamoRepositoryEnMemoria implements PrestamoRepository {

    private final List<Prestamo> prestamos = new ArrayList<>();
    private long contador = 0;

    @Override
    public long siguienteId() {
        contador++;
        return contador;
    }

    @Override
    public void guardar(Prestamo prestamo) {
        prestamos.add(prestamo);
    }

    List<Prestamo> todos() {
        return List.copyOf(prestamos);
    }
}
