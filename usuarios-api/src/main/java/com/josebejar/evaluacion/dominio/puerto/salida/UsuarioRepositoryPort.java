package com.josebejar.evaluacion.dominio.puerto.salida;

import com.josebejar.evaluacion.dominio.modelo.Usuario;

import java.util.Optional;

public interface UsuarioRepositoryPort {

    Optional<Usuario> buscarPorCorreo(String correo);

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorId(String id);

    void eliminarPorId(String id);
}
