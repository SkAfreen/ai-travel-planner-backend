package com.example.travelplanner.config;

import com.example.travelplanner.auth.JwtService;

import java.io.IOException;
import java.util.Collections;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        // No JWT provided: let Spring Security check endpoint permissions
        if (authorizationHeader == null) {

            filterChain.doFilter(request, response);
            return;
        }

        // Reject malformed Authorization headers
        if (!authorizationHeader.startsWith("Bearer ")) {

            rejectRequest(response, "Invalid Authorization header");
            return;
        }

        String token = authorizationHeader.substring(7).trim();

        // Reject invalid or expired JWT
        if (token.isEmpty() || !jwtService.isTokenValid(token)) {

            rejectRequest(response, "Invalid or expired JWT token");
            return;
        }

        String email = jwtService.extractEmail(token);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        Collections.emptyList()
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private void rejectRequest(
            HttpServletResponse response,
            String message) throws IOException {

        SecurityContextHolder.clearContext();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(
                "{\"error\":\"" + message + "\"}"
        );
    }
}