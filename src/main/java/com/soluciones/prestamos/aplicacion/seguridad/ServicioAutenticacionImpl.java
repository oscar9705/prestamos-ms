package com.soluciones.prestamos.aplicacion.seguridad;

import com.soluciones.prestamos.aplicacion.usuarios.ServicioAutenticacion;
import com.soluciones.prestamos.dominio.roles.Rol;
import com.soluciones.prestamos.dominio.usuarios.entidad.Usuario;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ServicioAutenticacionImpl implements ServicioAutenticacion {
    private final JdbcClient jdbcClient;

    public ServicioAutenticacionImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Usuario obtenerUsuarioPorUsername(String username) {
        Usuario usuario = jdbcClient.sql("select * from usuarios where username=:username limit 1")
                .param("username",username)
                .query(Usuario.class).optional().orElse(null);
        if(usuario != null) {
            Set<Rol> roles = jdbcClient.sql("""
                            SELECT r.codigo, r.descripcion
                            FROM usuarios u
                            JOIN usuarios_roles ur ON u.id = ur.usuario_id
                            JOIN roles r ON ur.rol_id = r.id
                            WHERE u.id = :id;
                            """)
                    .param("id", usuario.getId())
                    .query(Rol.class).set();
            usuario.setRoles(roles);
        }
        return usuario;
    }
}
