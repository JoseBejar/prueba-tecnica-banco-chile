package com.josebejar.evaluacion.application.service;

import com.josebejar.evaluacion.aplicacion.servicio.AutenticarUsuarioService;
import com.josebejar.evaluacion.dominio.modelo.Usuario;
import com.josebejar.evaluacion.dominio.puerto.salida.TokenProviderPort;
import com.josebejar.evaluacion.dominio.puerto.salida.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AutenticarUsuarioServiceTest {

    private UsuarioRepositoryPort usuarioRepositoryPort;
    private TokenProviderPort tokenProviderPort;
    private AutenticarUsuarioService autenticarUsuarioService;

    @BeforeEach
    void setUp() {
        usuarioRepositoryPort = mock(UsuarioRepositoryPort.class);
        tokenProviderPort = mock(TokenProviderPort.class);

        autenticarUsuarioService = new AutenticarUsuarioService(
                usuarioRepositoryPort,
                tokenProviderPort
        );
    }

    @Test
    void autenticar_deberiaAutenticarUsuarioCorrectamente() {

        Usuario usuarioExistente = Usuario.builder()
                .id("123")
                .correo("jose@test.com")
                .contrasena("Password123")
                .activo(true)
                .build();

        when(usuarioRepositoryPort.buscarPorCorreo("jose@test.com"))
                .thenReturn(Optional.of(usuarioExistente));

        when(tokenProviderPort.generarToken("123", "jose@test.com"))
                .thenReturn("TOKEN_LOGIN");

        when(usuarioRepositoryPort.guardar(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = autenticarUsuarioService.autenticar("jose@test.com", "Password123");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getToken()).isEqualTo("TOKEN_LOGIN");
        assertThat(resultado.getUltimoLogin()).isNotNull();
        assertThat(resultado.getModificado()).isNotNull();

        verify(usuarioRepositoryPort).guardar(any(Usuario.class));
    }

    @Test
    void autenticar_deberiaFallarSiUsuarioNoExiste() {

        when(usuarioRepositoryPort.buscarPorCorreo("no@existe.com"))
                .thenReturn(Optional.empty());


        assertThatThrownBy(() -> autenticarUsuarioService.autenticar("no@existe.com", "Password123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuario o contraseña inválidos");
    }

    @Test
    void autenticar_deberiaFallarSiContrasenaIncorrecta() {

        Usuario usuarioExistente = Usuario.builder()
                .id("123")
                .correo("jose@test.com")
                .contrasena("Password123")
                .build();

        when(usuarioRepositoryPort.buscarPorCorreo("jose@test.com"))
                .thenReturn(Optional.of(usuarioExistente));

        assertThatThrownBy(() -> autenticarUsuarioService.autenticar("jose@test.com", "OtraClave123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuario o contraseña inválidos");
    }
}
