package com.example.saqayatask.Configuration.app;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper(){
// Create a new instance of ModelMapper
        return new ModelMapper();
    }
}
