package com.benevenuto.queue_master.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.benevenuto.queue_master.domain.user.constants.UserRole;
import com.benevenuto.queue_master.domain.user.repositories.IUserRepository;
import com.benevenuto.queue_master.presentation.auth.dto.AuthDTO;
import com.benevenuto.queue_master.presentation.auth.dto.RegisterRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private IUserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Deve registrar um novo operador com sucesso quando o requisitante for um ADMIN")
    @WithMockUser(username = "admin@mail.com", authorities = {"ROLE_ADMIN", "ADMIN"})
    void shouldRegisterUserSuccessfully() throws Exception {
        RegisterRequestDTO registerDTO = new RegisterRequestDTO(
                "Operador Teste",
                "operador@mail.com",
                1234,
                "senhaSegura123",
                UserRole.OPERATOR
        );

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("operador@mail.com"));
    }

    @Test
    @DisplayName("Deve falhar ao tentar registrar se o usuário requisitante for um OPERATOR")
    @WithMockUser(username = "operator@mail.com", authorities = {"ROLE_OPERATOR", "OPERATOR"})
    void shouldFailToRegisterWhenUserIsOperator() throws Exception {
        RegisterRequestDTO registerDTO = new RegisterRequestDTO(
                "Invasor",
                "invasor.op@mail.com",
                999,
                "senhaSegura123",
                UserRole.OPERATOR
        );

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isForbidden()); 
    }

    @Test
    @DisplayName("Deve falhar ao tentar registrar se o usuário requisitante for um INVENTOR")
    @WithMockUser(username = "inventor@mail.com", authorities = {"ROLE_INVENTOR", "INVENTOR"})
    void shouldFailToRegisterWhenUserIsInventor() throws Exception {
        RegisterRequestDTO registerDTO = new RegisterRequestDTO(
                "Tentativa Inventor",
                "inventor.tentativa@mail.com",
                1002,
                "senhaSegura123",
                UserRole.OPERATOR
        );

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve realizar login com sucesso")
    void shouldLoginSuccessfully() throws Exception {
        // 1. Criamos e salvamos o usuário diretamente no banco H2 usando o repositório
        String email = "admin@mail.com";
        String senhaLimpa = "senhaValida123";
        
        // Garante que o banco está limpo para este e-mail antes de testar
        userRepository.findByEmail(email).ifPresent(u -> userRepository.deleteById(u.getId()));

        com.benevenuto.queue_master.domain.user.entity.User usuarioNoBanco = new com.benevenuto.queue_master.domain.user.entity.User(
                "Admin Teste Login",
                email,
                777, // Seu operatorNumber (Integer)
                passwordEncoder.encode(senhaLimpa), // CRUCIAL: A senha precisa ser criptografada aqui
                UserRole.ADMIN,
                true // active
        );
        
        // Salva direto no H2, pulando as validações de rota do UseCase
        userRepository.save(usuarioNoBanco);

        // 2. Tentamos logar de forma pública com as credenciais criadas
        AuthDTO authDTO = new AuthDTO(email, senhaLimpa);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    // Helper method corrigido para passar o Integer no operatorNumber
    private void createMockUserAsAdmin(String email, String password) throws Exception {
        RegisterRequestDTO registerDTO = new RegisterRequestDTO(
                "Usuario Teste", email, 777, password, UserRole.OPERATOR
        );

        mockMvc.perform(post("/auth/register")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("admin").authorities(new org.springframework.security.core.authority.SimpleGrantedAuthority("ADMIN")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk());
    }
}