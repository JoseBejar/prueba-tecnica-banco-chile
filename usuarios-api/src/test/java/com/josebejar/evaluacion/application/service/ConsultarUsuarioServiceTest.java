package com.josebejar.evaluacion.application.service;

import com.josebejar.evaluacion.aplicacion.servicio.ConsultarUsuarioService;
import com.josebejar.evaluacion.dominio.modelo.Usuario;
import com.josebejar.evaluacion.dominio.puerto.salida.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConsultarUsuarioServiceTest {

    private UsuarioRepositoryPort usuarioRepositoryPort;
    private ConsultarUsuarioService consultarUsuarioService;

    @BeforeEach
    void setUp() {
        usuarioRepositoryPort = mock(UsuarioRepositoryPort.class);
        consultarUsuarioService = new ConsultarUsuarioService(usuarioRepositoryPort);
    }

    @Test
    void obtenerPorId_deberiaRetornarUsuarioCuandoExiste() {

        Usuario usuario = Usuario.builder()
                .id("123")
                .nombre("Jose")
                .correo("jose@test.com")
                .build();

        when(usuarioRepositoryPort.buscarPorId("123"))
                .thenReturn(Optional.of(usuario));

        Usuario resultado = consultarUsuarioService.obtenerPorId("123");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo("123");
        assertThat(resultado.getCorreo()).isEqualTo("jose@test.com");
    }

    @Test
    void obtenerPorId_deberiaLanzarExcepcionCuandoNoExiste() {

        when(usuarioRepositoryPort.buscarPorId("999"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> consultarUsuarioService.obtenerPorId("999"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuario no encontrado");
    }
}
