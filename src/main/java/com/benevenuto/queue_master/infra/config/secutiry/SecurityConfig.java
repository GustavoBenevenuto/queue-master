package com.benevenuto.queue_master.infra.config.secutiry;

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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    // Construtor para injeção via Dependency Injection (sem @Autowired)
    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    /**
     * Configura a cadeia de filtros de segurança.
     * Responsabilidade única: definir regras de autenticação e autorização.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // 1. Rotas Públicas
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers("/ws-queue/**").permitAll()

                        // 2. Administração (Criação de usuários)
                        .requestMatchers(HttpMethod.POST, "/auth/register").hasRole("ADMIN")

                        // 3. Regras de Negócio - /orders
                        // OPERATOR: Só faz POST. 
                        // INVENTOR e ADMIN: Fazem tudo (GET, POST, PUT, DELETE, etc)
                        .requestMatchers(HttpMethod.POST, "/orders/**").hasAnyRole("ADMIN", "INVENTOR", "OPERATOR")
                        .requestMatchers("/orders/**").hasAnyRole("ADMIN", "INVENTOR")

                        // 4. Qualquer outra rota exige autenticação
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Expõe o AuthenticationManager como bean para uso no domínio (ex: login use case).
     * Segue Dependency Inversion: depender de abstração.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Bean de codificador de senha.
     * Segue Single Responsibility: responsabilidade única de fornecer hashing seguro.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}