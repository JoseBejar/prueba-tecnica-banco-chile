package com.josebejar.evaluacion.application.service;

import com.josebejar.evaluacion.aplicacion.servicio.ActualizarEstadoUsuarioService;
import com.josebejar.evaluacion.dominio.modelo.Usuario;
import com.josebejar.evaluacion.dominio.puerto.entrada.command.ActualizarEstadoUsuarioCommand;
import com.josebejar.evaluacion.dominio.puerto.salida.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ActualizarEstadoUsuarioServiceTest {

    private UsuarioRepositoryPort usuarioRepositoryPort;
    private ActualizarEstadoUsuarioService actualizarEstadoUsuarioService;

    @BeforeEach
    void setUp() {
        usuarioRepositoryPort = mock(UsuarioRepositoryPort.class);
        actualizarEstadoUsuarioService = new ActualizarEstadoUsuarioService(usuarioRepositoryPort);
    }

    @Test
    void actualizarEstado_deberiaActualizarActivoYModificado() {

        Usuario existente = Usuario.builder()
                .id("123")
                .correo("jose@test.com")
                .activo(true)
                .build();

        when(usuarioRepositoryPort.buscarPorId("123"))
                .thenReturn(Optional.of(existente));

        when(usuarioRepositoryPort.guardar(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ActualizarEstadoUsuarioCommand command = ActualizarEstadoUsuarioCommand.builder()
                .activo(false)
                .build();

        Usuario resultado = actualizarEstadoUsuarioService.actualizarEstado("123", command);

        assertThat(resultado.getActivo()).isFalse();
        assertThat(resultado.getModificado()).isNotNull();

        verify(usuarioRepositoryPort).guardar(any(Usuario.class));
    }

    @Test
    void actualizarEstado_deberiaFallarSiUsuarioNoExiste() {

        when(usuarioRepositoryPort.buscarPorId("999"))
                .thenReturn(Optional.empty());

        ActualizarEstadoUsuarioCommand command = ActualizarEstadoUsuarioCommand.builder()
                .activo(false)
                .build();

        assertThatThrownBy(() -> actualizarEstadoUsuarioService.actualizarEstado("999", command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuario no encontrado");
    }
}
