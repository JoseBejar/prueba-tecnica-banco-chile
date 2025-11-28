package com.josebejar.evaluacion.infraestructura.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class UsuarioResponse {
    private String id;
    private String nombre;
    private String correo;

    @JsonProperty("contraseña")
    private String contraseña;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime creado;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modificado;

    @JsonProperty("ultimoLogin")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ultimoLogin;

    private String token;
    private Boolean activo;

    private List<TelefonoResponse> telefonos;
}
