package com.josebejar.evaluacion.aplicacion.servicio;

import com.josebejar.evaluacion.dominio.modelo.Usuario;
import com.josebejar.evaluacion.dominio.puerto.entrada.ConsultarUsuarioUseCase;
import com.josebejar.evaluacion.dominio.puerto.salida.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class ConsultarUsuarioService implements ConsultarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public ConsultarUsuarioService(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }
    @Override
    public Usuario obtenerPorId(String id) {
        return usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }
}
