package com.josebejar.evaluacion.infraestructura.persistencia.jpa.adapter;

import com.josebejar.evaluacion.dominio.modelo.Telefono;
import com.josebejar.evaluacion.dominio.modelo.Usuario;
import com.josebejar.evaluacion.dominio.puerto.salida.UsuarioRepositoryPort;
import com.josebejar.evaluacion.infraestructura.persistencia.jpa.entity.TelefonoEntity;
import com.josebejar.evaluacion.infraestructura.persistencia.jpa.entity.UsuarioEntity;
import com.josebejar.evaluacion.infraestructura.persistencia.jpa.repository.UsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository usuarioJpaRepository;
    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioJpaRepository.findByCorreo(correo)
                .map(this::mapearADominio);
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity entity = mapearAEntidad(usuario);
        if (entity.getTelefonos() != null) {
            entity.getTelefonos().forEach(t -> t.setUsuario(entity));
        }

        UsuarioEntity guardado = usuarioJpaRepository.save(entity);
        return mapearADominio(guardado);
    }

    @Override
    public Optional<Usuario> buscarPorId(String id) {
        return usuarioJpaRepository.findById(id)
                .map(this::mapearADominio);
    }

    @Override
    public void eliminarPorId(String id) {
        usuarioJpaRepository.deleteById(id);
    }

    private Usuario mapearADominio(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }

        return Usuario.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .correo(entity.getCorreo())
                .contrasena(entity.getContrasena())
                .creado(entity.getCreado())
                .modificado(entity.getModificado())
                .ultimoLogin(entity.getUltimoLogin())
                .token(entity.getToken())
                .activo(entity.getActivo())
                .telefonos(
                        entity.getTelefonos() == null
                                ? null
                                : entity.getTelefonos().stream()
                                .map(this::mapearTelefonoADominio)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private Telefono mapearTelefonoADominio(TelefonoEntity entity) {
        if (entity == null) {
            return null;
        }
        return Telefono.builder()
                .id(entity.getId())
                .numero(entity.getNumero())
                .codigoCiudad(entity.getCodigoCiudad())
                .codigoPais(entity.getCodigoPais())
                .build();
    }

    private UsuarioEntity mapearAEntidad(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioEntity entity = UsuarioEntity.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .contrasena(usuario.getContrasena())
                .creado(usuario.getCreado())
                .modificado(usuario.getModificado())
                .ultimoLogin(usuario.getUltimoLogin())
                .token(usuario.getToken())
                .activo(usuario.getActivo())
                .build();

        if (usuario.getTelefonos() != null) {
            entity.setTelefonos(
                    usuario.getTelefonos().stream()
                            .map(t -> mapearTelefonoAEntidad(t, entity))
                            .collect(Collectors.toList())
            );
        }

        return entity;
    }

    private TelefonoEntity mapearTelefonoAEntidad(Telefono telefono, UsuarioEntity usuarioEntity) {
        if (telefono == null) {
            return null;
        }
        return TelefonoEntity.builder()
                .id(telefono.getId())
                .numero(telefono.getNumero())
                .codigoCiudad(telefono.getCodigoCiudad())
                .codigoPais(telefono.getCodigoPais())
                .usuario(usuarioEntity)
                .build();
    }
}
