package com.josebejar.evaluacion.aplicacion.servicio;

import com.josebejar.evaluacion.dominio.modelo.Telefono;
import com.josebejar.evaluacion.dominio.modelo.Usuario;
import com.josebejar.evaluacion.dominio.puerto.entrada.ActualizarUsuarioUseCase;
import com.josebejar.evaluacion.dominio.puerto.entrada.command.ActualizarUsuarioCommand;
import com.josebejar.evaluacion.dominio.puerto.entrada.command.TelefonoCommand;
import com.josebejar.evaluacion.dominio.puerto.salida.UsuarioRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
@Service
public class ActualizarUsuarioService implements ActualizarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final Pattern patronCorreo;
    private final Pattern patronContrasena;

    public ActualizarUsuarioService(UsuarioRepositoryPort usuarioRepositoryPort,
                                    @Value("${app.regex.correo}") String regexCorreo,
                                    @Value("${app.regex.contrasena}") String regexContrasena) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.patronCorreo = Pattern.compile(regexCorreo);
        this.patronContrasena = Pattern.compile(regexContrasena);
    }
    @Override
    public Usuario actualizar(String id, ActualizarUsuarioCommand command) {

        Usuario usuario = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!patronCorreo.matcher(command.getCorreo()).matches()) {
            throw new IllegalArgumentException("El formato del correo es inválido");
        }

        if (!patronContrasena.matcher(command.getContrasena()).matches()) {
            throw new IllegalArgumentException("El formato de la contraseña es inválido");
        }

        if (!usuario.getCorreo().equalsIgnoreCase(command.getCorreo())) {
            usuarioRepositoryPort.buscarPorCorreo(command.getCorreo())
                    .ifPresent(u -> {
                        if (!u.getId().equals(usuario.getId())) {
                            throw new IllegalStateException("El correo ya está registrado");
                        }
                    });
        }
        usuario.setNombre(command.getNombre());
        usuario.setCorreo(command.getCorreo());
        usuario.setContrasena(command.getContrasena());
        usuario.setModificado(LocalDateTime.now());

        usuario.setTelefonos(
                command.getTelefonos().stream()
                        .map(this::mapearTelefono)
                        .collect(Collectors.toList())
        );

        return usuarioRepositoryPort.guardar(usuario);
    }

    private Telefono mapearTelefono(TelefonoCommand command) {
        return Telefono.builder()
                .numero(command.getNumero())
                .codigoCiudad(command.getCodigoCiudad())
                .codigoPais(command.getCodigoPais())
                .build();
    }
    }
