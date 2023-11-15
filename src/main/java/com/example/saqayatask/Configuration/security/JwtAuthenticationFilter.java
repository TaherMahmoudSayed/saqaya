package com.example.saqayatask.Configuration.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Check if the request path is a permitted path
            if (skipPath(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            // Extract the JWT token from the Authorization header
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String email;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                // If the Authorization header is missing or does not start with "Bearer ", proceed to the next filter
                filterChain.doFilter(request, response);
                return;
            }
            jwt = authHeader.substring(7); // Extract the JWT token without the "Bearer " prefix
            email = jwtService.extractEmail(jwt);

            // Check if the email is not null and there is no authenticated user in the SecurityContextHolder
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Load the UserDetails for the email
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (userDetails != null) {
                    // If the JWT token is valid for the user, create an authentication token and set it in the SecurityContextHolder
                    if (jwtService.isJwtValid(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } else {
                // If the email is null or there was already an authenticated user, send an unauthorized response
                response.setContentType(APPLICATION_JSON_VALUE);
                response.sendError(UNAUTHORIZED.value(), "Unauthorized user");
            }
        } catch (Exception ex) {
            // If any exception occurs, send a forbidden response with the error message
            response.setContentType(APPLICATION_JSON_VALUE);
            response.sendError(FORBIDDEN.value(), ex.getMessage());
            // You can send a custom response here if needed
        } finally {
            // Proceed to the next filter in the FilterChain
            filterChain.doFilter(request, response);
        }
    }

    private boolean skipPath(HttpServletRequest request) {
        // Check if the request path is "/api/v1/users/add" and the method is "POST"
        return "/api/v1/users/add".equals(request.getServletPath()) && "POST".equals(request.getMethod());
    }

}
