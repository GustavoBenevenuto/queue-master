package com.benevenuto.queue_master.infra.config.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.benevenuto.queue_master.application.user.CreateNewUserUseCase;
import com.benevenuto.queue_master.domain.user.repositories.IUserRepository;
import com.benevenuto.queue_master.infra.config.secutiry.UserSecurityService;

@Configuration
public class UserConfig {

    /**
     * Instantiates the CreateNewUserUseCase with its required dependencies.
     * The PasswordEncoder bean is provided by SecurityConfig.
     */
    @Bean
    public CreateNewUserUseCase createNewUserUseCase(
            IUserRepository userRepository, 
            PasswordEncoder passwordEncoder
    ) {
        return new CreateNewUserUseCase(userRepository, passwordEncoder);
    }
    
    @Bean
    public UserSecurityService userSecurityService(
            IUserRepository userRepository
    ) {
        return new UserSecurityService(userRepository);
    }
}