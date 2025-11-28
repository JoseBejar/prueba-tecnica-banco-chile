package com.josebejar.evaluacion.application.service;

import com.josebejar.evaluacion.aplicacion.servicio.EliminarUsuarioService;
import com.josebejar.evaluacion.dominio.modelo.Usuario;
import com.josebejar.evaluacion.dominio.puerto.salida.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EliminarUsuarioServiceTest {

    private UsuarioRepositoryPort usuarioRepositoryPort;
    private EliminarUsuarioService eliminarUsuarioService;

    @BeforeEach
    void setUp() {
        usuarioRepositoryPort = mock(UsuarioRepositoryPort.class);
        eliminarUsuarioService = new EliminarUsuarioService(usuarioRepositoryPort);
    }

    @Test
    void eliminar_deberiaEliminarCuandoUsuarioExiste() {

        Usuario existente = Usuario.builder()
                .id("123")
                .correo("jose@test.com")
                .build();

        when(usuarioRepositoryPort.buscarPorId("123"))
                .thenReturn(Optional.of(existente));

        eliminarUsuarioService.eliminar("123");
        verify(usuarioRepositoryPort).eliminarPorId("123");
    }

    @Test
    void eliminar_deberiaFallarSiUsuarioNoExiste() {

        when(usuarioRepositoryPort.buscarPorId("999"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> eliminarUsuarioService.eliminar("999"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuario no encontrado");
    }
}
