package com.josebejar.evaluacion.aplicacion.servicio;

import com.josebejar.evaluacion.dominio.puerto.entrada.EliminarUsuarioUseCase;
import com.josebejar.evaluacion.dominio.puerto.salida.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class EliminarUsuarioService implements EliminarUsuarioUseCase {
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public EliminarUsuarioService(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public void eliminar(String id) {

        var existente = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuarioRepositoryPort.eliminarPorId(existente.getId());
    }
}
