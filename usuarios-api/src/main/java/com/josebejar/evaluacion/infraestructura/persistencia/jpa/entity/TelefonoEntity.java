package com.josebejar.evaluacion.infraestructura.persistencia.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "telefonos")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TelefonoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, length = 30)
    private String numero;

    @Column(name = "codigo_ciudad", nullable = false, length = 10)
    private String codigoCiudad;

    @Column(name = "codigo_pais", nullable = false, length = 10)
    private String codigoPais;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;
}
