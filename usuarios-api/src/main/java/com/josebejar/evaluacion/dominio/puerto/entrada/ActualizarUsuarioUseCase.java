package com.josebejar.evaluacion.dominio.puerto.entrada;

import com.josebejar.evaluacion.dominio.modelo.Usuario;
import com.josebejar.evaluacion.dominio.puerto.entrada.command.ActualizarUsuarioCommand;

public interface ActualizarUsuarioUseCase {
    Usuario actualizar(String id, ActualizarUsuarioCommand command);
}
