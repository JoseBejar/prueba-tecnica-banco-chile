package com.josebejar.evaluacion.infraestructura.persistencia.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "usuarios",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_usuarios_correo", columnNames = "correo")
        }
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private String id; // usamos String para alinearlo con el dominio

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "correo", nullable = false, length = 150)
    private String correo;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;

    @Column(name = "creado", nullable = false)
    private LocalDateTime creado;

    @Column(name = "modificado", nullable = false)
    private LocalDateTime modificado;

    @Column(name = "ultimo_login", nullable = false)
    private LocalDateTime ultimoLogin;

    @Column(name = "token", length = 500)
    private String token;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(
            mappedBy = "usuario",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<TelefonoEntity> telefonos = new ArrayList<>();

    public void addTelefono(TelefonoEntity telefono) {
        this.telefonos.add(telefono);
        telefono.setUsuario(this);
    }

    public void removeTelefono(TelefonoEntity telefono) {
        this.telefonos.remove(telefono);
        telefono.setUsuario(null);
    }
}
