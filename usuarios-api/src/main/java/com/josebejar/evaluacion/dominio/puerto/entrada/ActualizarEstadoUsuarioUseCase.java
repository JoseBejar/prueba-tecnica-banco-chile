package com.josebejar.evaluacion.dominio.puerto.entrada;

import com.josebejar.evaluacion.dominio.modelo.Usuario;
import com.josebejar.evaluacion.dominio.puerto.entrada.command.ActualizarEstadoUsuarioCommand;

public interface ActualizarEstadoUsuarioUseCase {

    Usuario actualizarEstado(String id, ActualizarEstadoUsuarioCommand command);
}
