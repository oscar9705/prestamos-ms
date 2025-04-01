package com.soluciones.prestamos.dominio.usuarios.puerto;

import com.soluciones.prestamos.dominio.usuarios.entidad.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    Optional<Usuario> findByUsername(String username);
}
