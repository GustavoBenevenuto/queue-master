package com.benevenuto.queue_master.infra.config.security;

import java.io.IOException;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.benevenuto.queue_master.domain.user.entity.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
	private final TokenService tokenService;
    private final UserSecurityService userSecurityService;

    public SecurityFilter(TokenService tokenService, UserSecurityService userSecurityService) {
        this.tokenService = tokenService;
        this.userSecurityService = userSecurityService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = recoverToken(request);

        if (token != null && !token.isEmpty()) {
            String subjectEmail = tokenService.validateToken(token);

            if (subjectEmail != null && !subjectEmail.isEmpty()) {
                UserDetails userDetails = userSecurityService.loadUserByUsername(subjectEmail);

                User user = (User) userDetails;
                
                if (user != null && !user.getActive()) {
                    throw new DisabledException("User is inactive and cannot authenticate");
                }
                
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Recupera o token JWT do cabeçalho Authorization.
     *
     * @param request requisição HTTP
     * @return token JWT ou null se não existir
     */
    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7); // Remove "Bearer " do início
    }
}
