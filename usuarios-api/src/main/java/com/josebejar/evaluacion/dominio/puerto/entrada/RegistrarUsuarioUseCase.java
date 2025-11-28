package com.josebejar.evaluacion.dominio.puerto.entrada;

import com.josebejar.evaluacion.dominio.modelo.Usuario;
import com.josebejar.evaluacion.dominio.puerto.entrada.command.RegistrarUsuarioCommand;

public interface RegistrarUsuarioUseCase {

    Usuario registrar(RegistrarUsuarioCommand command);
}
