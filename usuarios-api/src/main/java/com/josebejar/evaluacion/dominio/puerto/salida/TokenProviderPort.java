package com.josebejar.evaluacion.dominio.puerto.salida;

public interface TokenProviderPort {

    String generarToken(String usuarioId, String correo);
}
