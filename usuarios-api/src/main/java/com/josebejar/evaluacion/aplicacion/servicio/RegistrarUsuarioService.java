package com.josebejar.evaluacion.aplicacion.servicio;

import com.josebejar.evaluacion.dominio.modelo.Telefono;
import com.josebejar.evaluacion.dominio.modelo.Usuario;
import com.josebejar.evaluacion.dominio.puerto.entrada.RegistrarUsuarioUseCase;
import com.josebejar.evaluacion.dominio.puerto.entrada.command.RegistrarUsuarioCommand;
import com.josebejar.evaluacion.dominio.puerto.entrada.command.TelefonoCommand;
import com.josebejar.evaluacion.dominio.puerto.salida.TokenProviderPort;
import com.josebejar.evaluacion.dominio.puerto.salida.UsuarioRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RegistrarUsuarioService implements RegistrarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final TokenProviderPort tokenProviderPort;

    private final Pattern patronCorreo;
    private final Pattern patronContrasena;

    public RegistrarUsuarioService(UsuarioRepositoryPort usuarioRepositoryPort,
                                   TokenProviderPort tokenProviderPort,
                                   @Value("${app.regex.correo}") String regexCorreo,
                                   @Value("${app.regex.contrasena}") String regexContrasena) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.tokenProviderPort = tokenProviderPort;
        this.patronCorreo = Pattern.compile(regexCorreo);
        this.patronContrasena = Pattern.compile(regexContrasena);
    }

    @Override
    public Usuario registrar(RegistrarUsuarioCommand command) {

        if (!patronCorreo.matcher(command.getCorreo()).matches()) {
            throw new IllegalArgumentException("El formato del correo es inválido");
        }

        if (!patronContrasena.matcher(command.getContrasena()).matches()) {
            throw new IllegalArgumentException("El formato de la contraseña es inválido");
        }

        usuarioRepositoryPort.buscarPorCorreo(command.getCorreo())
                .ifPresent(u -> {
                    throw new IllegalStateException("El correo ya está registrado");
                });

        LocalDateTime ahora = LocalDateTime.now();

        Usuario usuario = Usuario.builder()
                .id(UUID.randomUUID().toString())
                .nombre(command.getNombre())
                .correo(command.getCorreo())
                .contrasena(command.getContrasena())
                .creado(ahora)
                .modificado(ahora)
                .ultimoLogin(ahora)
                .activo(true)
                .telefonos(
                        command.getTelefonos().stream()
                                .map(this::mapearTelefono)
                                .collect(Collectors.toList())
                )
                .build();
        String token = tokenProviderPort.generarToken(usuario.getId(), usuario.getCorreo());
        usuario.setToken(token);

        Usuario guardado = usuarioRepositoryPort.guardar(usuario);
        return guardado;
    }

    private Telefono mapearTelefono(TelefonoCommand command) {
        return Telefono.builder()
                .numero(command.getNumero())
                .codigoCiudad(command.getCodigoCiudad())
                .codigoPais(command.getCodigoPais())
                .build();
    }
}
