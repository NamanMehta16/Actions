package com.example.crudApp.config;

import com.example.crudApp.service.JWTService;
import com.example.crudApp.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JWTService jwtService;

    @Autowired
    UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Get Authorization header
        String authHeader =
                request.getHeader("Authorization");

        String token = null;
        String username = null;

        // 2. Check whether Bearer token exists
        if (authHeader != null &&
                authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);

            // 3. Extract username from JWT
            username = jwtService.extractUsername(token);
        }

        // 4. If username exists and user isn't already authenticated
        if (username != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            // 5. Load user from database
            UserDetails userDetails =
                    userService.loadUserByUsername(username);

            // 6. Validate JWT
            if (jwtService.validateToken(
                    token,
                    userDetails)) {

                // 7. Create Authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // 8. Add request details
                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // 9. Tell Spring Security user is authenticated
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }
        }

        // 10. Continue the request
        filterChain.doFilter(request, response);
    }
}