package com.josebejar.evaluacion.aplicacion.servicio;

import com.josebejar.evaluacion.dominio.modelo.Usuario;
import com.josebejar.evaluacion.dominio.puerto.entrada.ActualizarEstadoUsuarioUseCase;
import com.josebejar.evaluacion.dominio.puerto.entrada.command.ActualizarEstadoUsuarioCommand;
import com.josebejar.evaluacion.dominio.puerto.salida.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ActualizarEstadoUsuarioService implements ActualizarEstadoUsuarioUseCase {
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public ActualizarEstadoUsuarioService(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public Usuario actualizarEstado(String id, ActualizarEstadoUsuarioCommand command) {

        Usuario usuario = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuario.setActivo(command.getActivo());
        usuario.setModificado(LocalDateTime.now());

        return usuarioRepositoryPort.guardar(usuario);
    }
}
