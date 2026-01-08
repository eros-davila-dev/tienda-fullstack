package com.example.demo.config;

import com.example.demo.service.CustomUserDetailsService; // O usa UserDetailsService genérico
import com.example.demo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; // Usamos la interfaz genérica

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Obtener el cabecera de autorización (Donde viaja el token)
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Revisar si el token existe y empieza con "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token (quitamos la palabra "Bearer ")
        jwt = authHeader.substring(7);

        // 4. Extraer el usuario del token
        userEmail = jwtService.extractUsername(jwt);

        // 5. Si hay usuario y NO está autenticado todavía en el sistema
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Cargamos los datos del usuario desde la BD
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 6. Validamos si el token es válido
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // Creamos la autorización oficial de Spring
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 7. ¡ESTA LÍNEA ES CLAVE! Registramos al usuario como "Logueado"
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 8. Pase lo que pase, continuamos con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}