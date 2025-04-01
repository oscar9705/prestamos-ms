package com.soluciones.prestamos.infraestructura.controladores;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

public class ControladorEjemplo {
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEndpoint() {
        return "Solo accesible para administradores";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String userEndpoint() {
        return "Accesible para usuarios y administradores";
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Accesible para todos";
    }
}
