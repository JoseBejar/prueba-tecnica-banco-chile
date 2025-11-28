package com.josebejar.evaluacion.infraestructura.web.controller;

import com.josebejar.evaluacion.dominio.modelo.Usuario;
import com.josebejar.evaluacion.dominio.puerto.entrada.AutenticarUsuarioUseCase;
import com.josebejar.evaluacion.infraestructura.web.dto.request.LoginRequest;
import com.josebejar.evaluacion.infraestructura.web.dto.response.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        Usuario usuario = autenticarUsuarioUseCase.autenticar(
                request.getCorreo(),
                request.getContraseña()
        );

        return LoginResponse.builder()
                .id(usuario.getId())
                .correo(usuario.getCorreo())
                .token(usuario.getToken())
                .ultimoLogin(usuario.getUltimoLogin())
                .build();
    }
}
