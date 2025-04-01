package com.soluciones.prestamos.infraestructura.seguridad;

import com.soluciones.prestamos.aplicacion.usuarios.ServicioAutenticacion;
import com.soluciones.prestamos.dominio.usuarios.entidad.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ServicioAutenticacion servicioAutenticacion;

    public CustomUserDetailsService(ServicioAutenticacion servicioAutenticacion) {
        this.servicioAutenticacion = servicioAutenticacion;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = servicioAutenticacion.obtenerUsuarioPorUsername(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con el username: " + username);
        }

        Collection<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getCodigo()))
                .collect(Collectors.toList());

        return new User(usuario.getUsername(), usuario.getPassword(), usuario.isActivo(), true, true, true, authorities);
    }
}