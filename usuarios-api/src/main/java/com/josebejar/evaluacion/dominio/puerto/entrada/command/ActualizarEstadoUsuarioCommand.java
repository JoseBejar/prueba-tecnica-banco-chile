package com.josebejar.evaluacion.dominio.puerto.entrada.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActualizarEstadoUsuarioCommand {

    private Boolean activo;
}
