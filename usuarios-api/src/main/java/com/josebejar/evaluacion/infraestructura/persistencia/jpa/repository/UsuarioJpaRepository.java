package com.josebejar.evaluacion.infraestructura.persistencia.jpa.repository;

import com.josebejar.evaluacion.infraestructura.persistencia.jpa.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, String> {

    Optional<UsuarioEntity> findByCorreo(String correo);
}
