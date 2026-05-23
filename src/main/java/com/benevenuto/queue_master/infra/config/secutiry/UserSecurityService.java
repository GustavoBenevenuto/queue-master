package com.benevenuto.queue_master.infra.config.secutiry;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.benevenuto.queue_master.domain.user.repositories.IUserRepository;

public class UserSecurityService implements UserDetailsService {

	private final IUserRepository userRepository;

    public UserSecurityService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Método obrigatório de UserDetailsService.
     * É usado pelo Spring Security para carregar o usuário pelo username (ou email).
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
