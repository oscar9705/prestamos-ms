package com.soluciones.prestamos.aplicacion.usuarios;

import com.soluciones.prestamos.dominio.usuarios.entidad.Usuario;

public interface ServicioAutenticacion {
    Usuario obtenerUsuarioPorUsername(String username);
}