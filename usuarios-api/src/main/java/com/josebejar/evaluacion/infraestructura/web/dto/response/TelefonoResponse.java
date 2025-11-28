package com.josebejar.evaluacion.infraestructura.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TelefonoResponse {

    private Long id;
    private String numero;
    private String codigoCiudad;
    private String codigoPais;
}
