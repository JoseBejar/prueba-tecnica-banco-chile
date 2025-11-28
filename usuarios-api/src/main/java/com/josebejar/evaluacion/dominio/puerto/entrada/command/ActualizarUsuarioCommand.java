package com.josebejar.evaluacion.dominio.puerto.entrada.command;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
public class ActualizarUsuarioCommand {

    private String nombre;
    private String correo;
    private String contrasena;
    private List<TelefonoCommand> telefonos;
}
