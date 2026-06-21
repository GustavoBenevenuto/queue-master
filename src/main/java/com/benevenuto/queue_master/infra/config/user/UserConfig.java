package com.benevenuto.queue_master.infra.config.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.benevenuto.queue_master.application.user.ChangeUserPasswordUseCase;
import com.benevenuto.queue_master.application.user.CreateNewUserUseCase;
import com.benevenuto.queue_master.application.user.DeleteUserUseCase;
import com.benevenuto.queue_master.application.user.GetAllUsersUseCase;
import com.benevenuto.queue_master.application.user.UpdateUserUseCase;
import com.benevenuto.queue_master.domain.user.repository.IUserRepository;
import com.benevenuto.queue_master.infra.config.security.UserSecurityService;

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
    public GetAllUsersUseCase getAllUsersUseCase(
            IUserRepository userRepository
    ) {
        return new GetAllUsersUseCase(userRepository);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(
            IUserRepository userRepository
    ) {
        return new UpdateUserUseCase(userRepository);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(
            IUserRepository userRepository
    ) {
        return new DeleteUserUseCase(userRepository);
    }

    @Bean
    public ChangeUserPasswordUseCase changeUserPasswordUseCase(
            IUserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return new ChangeUserPasswordUseCase(userRepository, passwordEncoder);
    }

    @Bean
    public UserSecurityService userSecurityService(
            IUserRepository userRepository
    ) {
        return new UserSecurityService(userRepository);
    }
}