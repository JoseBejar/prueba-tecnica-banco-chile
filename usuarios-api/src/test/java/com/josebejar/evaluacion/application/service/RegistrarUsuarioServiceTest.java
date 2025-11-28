package com.josebejar.evaluacion.application.service;

import com.josebejar.evaluacion.aplicacion.servicio.RegistrarUsuarioService;
import com.josebejar.evaluacion.dominio.modelo.Usuario;
import com.josebejar.evaluacion.dominio.puerto.entrada.command.RegistrarUsuarioCommand;
import com.josebejar.evaluacion.dominio.puerto.entrada.command.TelefonoCommand;
import com.josebejar.evaluacion.dominio.puerto.salida.TokenProviderPort;
import com.josebejar.evaluacion.dominio.puerto.salida.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class RegistrarUsuarioServiceTest {

    private UsuarioRepositoryPort usuarioRepositoryPort;
    private TokenProviderPort tokenProviderPort;
    private RegistrarUsuarioService registrarUsuarioService;

    @BeforeEach
    void setUp() {
        usuarioRepositoryPort = mock(UsuarioRepositoryPort.class);
        tokenProviderPort = mock(TokenProviderPort.class);

        String regexCorreo = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
        String regexContrasena = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";

        registrarUsuarioService = new RegistrarUsuarioService(
                usuarioRepositoryPort,
                tokenProviderPort,
                regexCorreo,
                regexContrasena
        );
    }

    @Test
    void registrar_deberiaCrearUsuarioCorrectamente() {

        RegistrarUsuarioCommand command = RegistrarUsuarioCommand.builder()
                .nombre("Jose Luis")
                .correo("jose@test.com")
                .contrasena("Password123")
                .telefonos(Collections.singletonList(
                        TelefonoCommand.builder()
                                .numero("1234567")
                                .codigoCiudad("1")
                                .codigoPais("57")
                                .build()
                ))
                .build();

        when(usuarioRepositoryPort.buscarPorCorreo("jose@test.com"))
                .thenReturn(Optional.empty());

        when(tokenProviderPort.generarToken(any(), any()))
                .thenReturn("TOKEN_FAKE");

        when(usuarioRepositoryPort.guardar(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // devuelve el mismo

        Usuario resultado = registrarUsuarioService.registrar(command);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotBlank();
        assertThat(resultado.getCorreo()).isEqualTo("jose@test.com");
        assertThat(resultado.getToken()).isEqualTo("TOKEN_FAKE");
        assertThat(resultado.getActivo()).isTrue();
        assertThat(resultado.getTelefonos()).hasSize(1);
        assertThat(resultado.getCreado()).isNotNull();
        assertThat(resultado.getModificado()).isNotNull();
        assertThat(resultado.getUltimoLogin()).isNotNull();

        verify(usuarioRepositoryPort).guardar(any(Usuario.class));
    }

    @Test
    void registrar_deberiaFallarSiCorreoInvalido() {

        RegistrarUsuarioCommand command = RegistrarUsuarioCommand.builder()
                .nombre("Jose Luis")
                .correo("correo-invalido")
                .contrasena("Password123")
                .telefonos(Collections.emptyList())
                .build();

        assertThatThrownBy(() -> registrarUsuarioService.registrar(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El formato del correo es inválido");
    }

    @Test
    void registrar_deberiaFallarSiCorreoYaExiste() {

        RegistrarUsuarioCommand command = RegistrarUsuarioCommand.builder()
                .nombre("Jose Luis")
                .correo("jose@test.com")
                .contrasena("Password123")
                .telefonos(Collections.emptyList())
                .build();

        when(usuarioRepositoryPort.buscarPorCorreo("jose@test.com"))
                .thenReturn(Optional.of(Usuario.builder().id("X").build()));

        assertThatThrownBy(() -> registrarUsuarioService.registrar(command))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("El correo ya está registrado");
    }
}
