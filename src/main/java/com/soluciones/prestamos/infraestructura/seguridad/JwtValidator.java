package com.soluciones.prestamos.infraestructura.seguridad;

import com.soluciones.prestamos.infraestructura.configuracion.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

public class JwtValidator {
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public JwtValidator(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public boolean isTokenValid(String token, String username) {
        UserDetails userDetails = cargarInforUsuario(username);
        return jwtUtil.isTokenValid(token, userDetails);
    }

    public UserDetails cargarInforUsuario(String username) {
        return userDetailsService.loadUserByUsername(username);
    }
}