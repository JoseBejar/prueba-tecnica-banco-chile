package com.josebejar.evaluacion.application.service;

import com.josebejar.evaluacion.aplicacion.servicio.ActualizarUsuarioService;
import com.josebejar.evaluacion.dominio.modelo.Usuario;
import com.josebejar.evaluacion.dominio.puerto.entrada.command.ActualizarUsuarioCommand;
import com.josebejar.evaluacion.dominio.puerto.entrada.command.TelefonoCommand;
import com.josebejar.evaluacion.dominio.puerto.salida.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ActualizarUsuarioServiceTest {

    private UsuarioRepositoryPort usuarioRepositoryPort;
    private ActualizarUsuarioService actualizarUsuarioService;

    @BeforeEach
    void setUp() {
        usuarioRepositoryPort = mock(UsuarioRepositoryPort.class);

        String regexCorreo = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
        String regexContrasena = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";

        actualizarUsuarioService = new ActualizarUsuarioService(
                usuarioRepositoryPort,
                regexCorreo,
                regexContrasena
        );
    }

    @Test
    void actualizar_deberiaActualizarUsuarioCorrectamente() {
        // given
        Usuario existente = Usuario.builder()
                .id("123")
                .nombre("Jose")
                .correo("jose@test.com")
                .contrasena("Password123")
                .build();

        when(usuarioRepositoryPort.buscarPorId("123"))
                .thenReturn(Optional.of(existente));

        when(usuarioRepositoryPort.buscarPorCorreo("jose@test.com"))
                .thenReturn(Optional.of(existente)); // mismo usuario

        when(usuarioRepositoryPort.guardar(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ActualizarUsuarioCommand command = ActualizarUsuarioCommand.builder()
                .nombre("Jose Actualizado")
                .correo("jose@test.com")
                .contrasena("Password123")
                .telefonos(Collections.singletonList(
                        TelefonoCommand.builder()
                                .numero("7654321")
                                .codigoCiudad("1")
                                .codigoPais("57")
                                .build()
                ))
                .build();

        Usuario resultado = actualizarUsuarioService.actualizar("123", command);

        assertThat(resultado.getNombre()).isEqualTo("Jose Actualizado");
        assertThat(resultado.getTelefonos()).hasSize(1);
        assertThat(resultado.getModificado()).isNotNull();

        verify(usuarioRepositoryPort).guardar(any(Usuario.class));
    }

    @Test
    void actualizar_deberiaFallarSiUsuarioNoExiste() {

        when(usuarioRepositoryPort.buscarPorId("999"))
                .thenReturn(Optional.empty());

        ActualizarUsuarioCommand command = ActualizarUsuarioCommand.builder()
                .nombre("X")
                .correo("x@test.com")
                .contrasena("Password123")
                .telefonos(Collections.emptyList())
                .build();

        assertThatThrownBy(() -> actualizarUsuarioService.actualizar("999", command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuario no encontrado");
    }

    @Test
    void actualizar_deberiaFallarSiCorreoInvalido() {

        Usuario existente = Usuario.builder()
                .id("123")
                .nombre("Jose")
                .correo("jose@test.com")
                .build();

        when(usuarioRepositoryPort.buscarPorId("123"))
                .thenReturn(Optional.of(existente));

        ActualizarUsuarioCommand command = ActualizarUsuarioCommand.builder()
                .nombre("Jose")
                .correo("correo-malo")
                .contrasena("Password123")
                .telefonos(Collections.emptyList())
                .build();

        assertThatThrownBy(() -> actualizarUsuarioService.actualizar("123", command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El formato del correo es inválido");
    }

    @Test
    void actualizar_deberiaFallarSiCorreoYaExisteEnOtroUsuario() {

        Usuario existente = Usuario.builder()
                .id("123")
                .nombre("Jose")
                .correo("jose@test.com")
                .build();

        Usuario otro = Usuario.builder()
                .id("456")
                .correo("nuevo@test.com")
                .build();

        when(usuarioRepositoryPort.buscarPorId("123"))
                .thenReturn(Optional.of(existente));

        when(usuarioRepositoryPort.buscarPorCorreo("nuevo@test.com"))
                .thenReturn(Optional.of(otro));

        ActualizarUsuarioCommand command = ActualizarUsuarioCommand.builder()
                .nombre("Jose")
                .correo("nuevo@test.com")
                .contrasena("Password123")
                .telefonos(Collections.emptyList())
                .build();

        assertThatThrownBy(() -> actualizarUsuarioService.actualizar("123", command))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("El correo ya está registrado");
    }
}
