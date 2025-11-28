package com.josebejar.evaluacion.infraestructura.web.controller;

import com.josebejar.evaluacion.dominio.modelo.Telefono;
import com.josebejar.evaluacion.dominio.modelo.Usuario;
import com.josebejar.evaluacion.dominio.puerto.entrada.*;
import com.josebejar.evaluacion.dominio.puerto.entrada.command.ActualizarEstadoUsuarioCommand;
import com.josebejar.evaluacion.dominio.puerto.entrada.command.ActualizarUsuarioCommand;
import com.josebejar.evaluacion.dominio.puerto.entrada.command.RegistrarUsuarioCommand;
import com.josebejar.evaluacion.dominio.puerto.entrada.command.TelefonoCommand;
import com.josebejar.evaluacion.infraestructura.web.dto.request.ActualizarEstadoUsuarioRequest;
import com.josebejar.evaluacion.infraestructura.web.dto.request.TelefonoRequest;
import com.josebejar.evaluacion.infraestructura.web.dto.request.UsuarioRequest;
import com.josebejar.evaluacion.infraestructura.web.dto.response.TelefonoResponse;
import com.josebejar.evaluacion.infraestructura.web.dto.response.UsuarioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
    private final ConsultarUsuarioUseCase consultarUsuarioUseCase;
    private final ActualizarUsuarioUseCase actualizarUsuarioUseCase;
    private final ActualizarEstadoUsuarioUseCase actualizarEstadoUsuarioUseCase;
    private final EliminarUsuarioUseCase eliminarUsuarioUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse crearUsuario(@Valid @RequestBody UsuarioRequest request) {

        RegistrarUsuarioCommand command = mapearACommand(request);
        Usuario usuarioCreado = registrarUsuarioUseCase.registrar(command);
        return mapearAResponse(usuarioCreado);
    }

    @GetMapping("/{id}")
    public UsuarioResponse obtenerUsuario(@PathVariable String id) {
        Usuario usuario = consultarUsuarioUseCase.obtenerPorId(id);
        return mapearAResponse(usuario);
    }

    @PutMapping("/{id}")
    public UsuarioResponse actualizarUsuario(@PathVariable String id,
                                             @Valid @RequestBody UsuarioRequest request) {

        ActualizarUsuarioCommand command = mapearAActualizarCommand(request);
        Usuario usuarioActualizado = actualizarUsuarioUseCase.actualizar(id, command);
        return mapearAResponse(usuarioActualizado);
    }

    @PatchMapping("/{id}")
    public UsuarioResponse actualizarEstado(@PathVariable String id,
                                            @Valid @RequestBody ActualizarEstadoUsuarioRequest request) {

        ActualizarEstadoUsuarioCommand command = ActualizarEstadoUsuarioCommand.builder()
                .activo(request.getActivo())
                .build();

        Usuario usuarioActualizado = actualizarEstadoUsuarioUseCase.actualizarEstado(id, command);

        return mapearAResponse(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarUsuario(@PathVariable String id) {
        eliminarUsuarioUseCase.eliminar(id);
    }


    private RegistrarUsuarioCommand mapearACommand(UsuarioRequest request) {
        return RegistrarUsuarioCommand.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .contrasena(request.getContraseña())
                .telefonos(
                        request.getTelefonos().stream()
                                .map(this::mapearTelefonoACommand)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private TelefonoCommand mapearTelefonoACommand(TelefonoRequest request) {
        return TelefonoCommand.builder()
                .numero(request.getNumero())
                .codigoCiudad(request.getCodigoCiudad())
                .codigoPais(request.getCodigoPais())
                .build();
    }

    private UsuarioResponse mapearAResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .contraseña(usuario.getContrasena())
                .creado(usuario.getCreado())
                .modificado(usuario.getModificado())
                .ultimoLogin(usuario.getUltimoLogin())
                .token(usuario.getToken())
                .activo(usuario.getActivo())
                .telefonos(
                        usuario.getTelefonos() == null
                                ? null
                                : usuario.getTelefonos().stream()
                                .map(this::mapearTelefonoAResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private TelefonoResponse mapearTelefonoAResponse(Telefono telefono) {
        return TelefonoResponse.builder()
                .id(telefono.getId())
                .numero(telefono.getNumero())
                .codigoCiudad(telefono.getCodigoCiudad())
                .codigoPais(telefono.getCodigoPais())
                .build();
    }

    private ActualizarUsuarioCommand mapearAActualizarCommand(UsuarioRequest request) {
        return ActualizarUsuarioCommand.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .contrasena(request.getContraseña())
                .telefonos(
                        request.getTelefonos().stream()
                                .map(this::mapearTelefonoACommand)
                                .collect(Collectors.toList())
                )
                .build();
    }



}
