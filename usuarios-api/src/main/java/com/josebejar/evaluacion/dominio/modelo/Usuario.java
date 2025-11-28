package com.josebejar.evaluacion.dominio.modelo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class Usuario {

    private String id;
    private String nombre;
    private String correo;
    private String contrasena;
    private LocalDateTime creado;
    private LocalDateTime modificado;
    private LocalDateTime ultimoLogin;
    private String token;
    private Boolean activo;


    @Builder.Default
    private List<Telefono> telefonos = new ArrayList<>();

    public void agregarTelefono(Telefono telefono) {
        this.telefonos.add(telefono);
    }

    public void marcarActivo() {
        this.activo = Boolean.TRUE;
    }

    public void actualizarFechasAlCrear(LocalDateTime ahora) {
        this.creado = ahora;
        this.modificado = ahora;
        this.ultimoLogin = ahora;
    }

    public void actualizarFechaModificado(LocalDateTime ahora) {
        this.modificado = ahora;
    }
}
