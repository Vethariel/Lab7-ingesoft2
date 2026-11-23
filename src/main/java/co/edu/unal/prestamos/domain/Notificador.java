package co.edu.unal.prestamos.domain;

/** Puerto de salida: avisa al estudiante (correo, push, etc.). */
public interface Notificador {

    void notificar(String estudianteId, String mensaje);
}
