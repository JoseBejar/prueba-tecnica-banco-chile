package com.josebejar.evaluacion.aplicacion.servicio;

import com.josebejar.evaluacion.dominio.modelo.Usuario;
import com.josebejar.evaluacion.dominio.puerto.entrada.AutenticarUsuarioUseCase;
import com.josebejar.evaluacion.dominio.puerto.salida.TokenProviderPort;
import com.josebejar.evaluacion.dominio.puerto.salida.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AutenticarUsuarioService implements AutenticarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final TokenProviderPort tokenProviderPort;

    public AutenticarUsuarioService(UsuarioRepositoryPort usuarioRepositoryPort,
                                    TokenProviderPort tokenProviderPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.tokenProviderPort = tokenProviderPort;
    }

    @Override
    public Usuario autenticar(String correo, String contrasena) {

        Usuario usuario = usuarioRepositoryPort.buscarPorCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException("Usuario o contraseña inválidos"));

        if (!usuario.getContrasena().equals(contrasena)) {
            throw new IllegalArgumentException("Usuario o contraseña inválidos");
        }

        LocalDateTime ahora = LocalDateTime.now();
        usuario.setUltimoLogin(ahora);
        usuario.setModificado(ahora);

        String tokenNuevo = tokenProviderPort.generarToken(usuario.getId(), usuario.getCorreo());
        usuario.setToken(tokenNuevo);

        return usuarioRepositoryPort.guardar(usuario);
    }
}
