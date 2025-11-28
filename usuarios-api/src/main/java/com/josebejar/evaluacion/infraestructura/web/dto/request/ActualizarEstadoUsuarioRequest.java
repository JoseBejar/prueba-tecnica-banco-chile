package com.josebejar.evaluacion.infraestructura.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarEstadoUsuarioRequest {
    @NotNull(message = "Debe indicar el estado del usuario")
    private Boolean activo;

    public ActualizarEstadoUsuarioRequest() {
    }
}
