package com.josebejar.evaluacion.infraestructura.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TelefonoRequest {

    @NotBlank(message = "El número de teléfono es obligatorio")
    @JsonProperty("numero")
    private String numero;

    @NotBlank(message = "El código de ciudad es obligatorio")
    @JsonProperty("codigoCiudad")
    private String codigoCiudad;

    @NotBlank(message = "El código de país es obligatorio")
    @JsonProperty("codigoPais")
    private String codigoPais;

    public TelefonoRequest() {
    }
}
