package com.benevenuto.queue_master.infra.config.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    /**
     * Configura a cadeia de filtros de segurança.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // 1. Rotas Públicas (Todos acessam o login e os canais iniciais do WS)
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers("/ws-queue/**").permitAll()

                        // 2. Administração (SÓ ADMIN acessa o register)
                        .requestMatchers(HttpMethod.POST, "/auth/register").hasRole("ADMIN")

                        // 2.1 Usuários (/users): troca de senha é liberada para qualquer usuário
                        // autenticado (o próprio usecase garante que só altera a senha de si mesmo);
                        // as demais operações de gestão de usuários são restritas a ADMIN
                        .requestMatchers(HttpMethod.PATCH, "/users/*/password").authenticated()
                        .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")

                        // 3. Regras de Negócio - /orders (Ordem estrita das rotas mais específicas para
                        // as gerais)

                        // OPERATOR: Pode cadastrar ordens (POST), mudar status (PATCH) e listar as
                        // próprias (GET por operador)
                        .requestMatchers(HttpMethod.POST, "/orders/**").hasAnyRole("ADMIN", "INVENTOR", "OPERATOR")
                        .requestMatchers(HttpMethod.PATCH, "/orders/**").hasAnyRole("ADMIN", "INVENTOR", "OPERATOR")
                        .requestMatchers(HttpMethod.GET, "/orders/*/operator/*")
                        .hasAnyRole("ADMIN", "INVENTOR", "OPERATOR")

                        // LISTA GERAL: Operador NÃO acessa a rota de trazer todos (Apenas ADMIN e
                        // INVENTOR)
                        // DELETAR: Apenas ADMIN e INVENTOR deletam itens
                        .requestMatchers(HttpMethod.GET, "/orders/**").hasAnyRole("ADMIN", "INVENTOR")
                        .requestMatchers(HttpMethod.DELETE, "/orders/**").hasAnyRole("ADMIN", "INVENTOR")

                        // 4. Qualquer outra rota exige autenticação genérica
                        .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permite que o Next.js acesse a API
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000"));

        // Permite os métodos HTTP que você vai usar
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Permite que o cabeçalho Authorization e o Content-Type passem na requisição
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // Importante caso no futuro decida usar cookies cruzados, se não, pode deixar
        // true mesmo assim
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica essa liberação para todas as rotas da API
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}