package com.josebejar.evaluacion.infraestructura.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String mensaje;
}
