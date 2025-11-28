package com.josebejar.evaluacion.dominio.modelo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Telefono {

    private Long id;
    private String numero;
    private String codigoCiudad;
    private String codigoPais;

}
