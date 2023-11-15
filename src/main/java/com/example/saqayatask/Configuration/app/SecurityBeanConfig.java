package com.example.saqayatask.Configuration.app;

import com.example.saqayatask.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityBeanConfig {
    private final UserRepo userRepository;

    @Bean
    public UserDetailsService userDetailsService(){
        // The UserDetailsService is a functional interface, allowing us to use a lambda expression with it
        return email ->
                userRepository.findByEmail(email).orElse(null);
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        // Create a new instance of DaoAuthenticationProvider
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // Set the UserDetailsService for the DaoAuthenticationProvider
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        // Set the PasswordEncoder for the DaoAuthenticationProvider
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        // Return the configured DaoAuthenticationProvider as an AuthenticationProvider
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // Return the AuthenticationManager from the AuthenticationConfiguration
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Create and return a new instance of BCryptPasswordEncoder as the PasswordEncoder
        return new BCryptPasswordEncoder();
    }


}
