package com.benevenuto.queue_master.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Obrigatório para conseguir fazer POST e PUT
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Libera qualquer rota sem login
            )
            .formLogin(form -> form.disable()) // Desativa a tela de login
            .httpBasic(basic -> basic.disable()); // Desativa a autenticação básica
            
        return http.build();
    }
}