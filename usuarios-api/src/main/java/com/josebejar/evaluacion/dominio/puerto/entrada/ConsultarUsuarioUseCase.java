package com.josebejar.evaluacion.dominio.puerto.entrada;

import com.josebejar.evaluacion.dominio.modelo.Usuario;

public interface ConsultarUsuarioUseCase {

    Usuario obtenerPorId(String id);
}
