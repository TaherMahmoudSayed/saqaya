package com.example.saqayatask.Configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {

    // Define private fields for the authentication provider and JWT filter
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // Disable Cross-Site Request Forgery (CSRF) protection
                .csrf().disable()
                // Configure authorization for HTTP requests
                .authorizeHttpRequests()
                // Allow unauthenticated access to the "/api/v1/users/" endpoint for POST requests
                .requestMatchers(HttpMethod.POST, "/api/v1/users/").permitAll()
                // Require authentication for all other requests
                .anyRequest().authenticated()
                .and()
                // Configure session management
                .sessionManagement()
                // Set the session creation policy to stateless (no sessions)
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // Configure exception handling
                .exceptionHandling()
                // Set the authentication entry point (how unauthenticated requests are handled)
                .authenticationEntryPoint(null)
                // Set the access denied handler (how denied requests are handled)
                .accessDeniedHandler(null)
                .and()
                // Register the authentication provider
                .authenticationProvider(authenticationProvider)
                // Add the JWT filter before the UsernamePasswordAuthenticationFilter in the filter chain
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Return the fully configured HttpSecurity object representing the security filter chain
        return httpSecurity.build();
    }
}
