package com.soluciones.prestamos.infraestructura.configuracion;

import com.soluciones.prestamos.aplicacion.usuarios.ServicioAutenticacion;
import com.soluciones.prestamos.infraestructura.seguridad.CustomUserDetailsService;
import com.soluciones.prestamos.infraestructura.seguridad.JwtAuthenticationFilter;
import com.soluciones.prestamos.infraestructura.seguridad.JwtValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {
    private final ServicioAutenticacion servicioAutenticacion;

    public ConfiguracionSeguridad(ServicioAutenticacion servicioAutenticacion) {
        this.servicioAutenticacion = servicioAutenticacion;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(servicioAutenticacion);
    }

    @Bean
    public JwtValidator jwtValidator(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        return new JwtValidator(userDetailsService, jwtUtil);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil, JwtValidator jwtValidator) {
        return new JwtAuthenticationFilter(jwtUtil, jwtValidator);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Permitir acceso a rutas de autenticación
                        .anyRequest().authenticated() // Requiere autenticación para el resto de las rutas
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter(jwtUtil(), jwtValidator(jwtUtil(), userDetailsService())), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }
}