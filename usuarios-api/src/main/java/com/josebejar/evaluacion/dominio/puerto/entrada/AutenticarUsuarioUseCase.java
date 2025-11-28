package com.josebejar.evaluacion.dominio.puerto.entrada;

import com.josebejar.evaluacion.dominio.modelo.Usuario;

public interface AutenticarUsuarioUseCase {

    Usuario autenticar(String correo, String contrasena);
}
