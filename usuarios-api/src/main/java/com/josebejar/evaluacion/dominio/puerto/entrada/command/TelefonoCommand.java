package com.josebejar.evaluacion.dominio.puerto.entrada.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TelefonoCommand {
    private String numero;
    private String codigoCiudad;
    private String codigoPais;
}
