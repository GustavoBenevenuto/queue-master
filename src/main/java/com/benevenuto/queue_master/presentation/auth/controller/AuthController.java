package com.benevenuto.queue_master.presentation.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.benevenuto.queue_master.application.user.CreateNewUserUseCase;
import com.benevenuto.queue_master.domain.user.dto.UserDTO;
import com.benevenuto.queue_master.domain.user.entity.User;
import com.benevenuto.queue_master.infra.config.security.TokenService;
import com.benevenuto.queue_master.presentation.auth.dto.AuthDTO;
import com.benevenuto.queue_master.presentation.auth.dto.LoginResponseDTO;
import com.benevenuto.queue_master.presentation.auth.dto.RegisterRequestDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
    private final CreateNewUserUseCase createNewUserUseCase;
    private final TokenService tokenService;

    public AuthController(
            AuthenticationManager authenticationManager,
            CreateNewUserUseCase createNewUserUseCase,
            TokenService tokenService
    ) {
        this.authenticationManager = authenticationManager;
        this.createNewUserUseCase = createNewUserUseCase;
        this.tokenService = tokenService;
    }
	
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody @Valid AuthDTO authDTO) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(authDTO.email(), authDTO.password());
		
		var authenticate = authenticationManager.authenticate(usernamePassword);
		
		var token = tokenService.generateToken((User) authenticate.getPrincipal());
		
		return ResponseEntity.ok(new LoginResponseDTO(token));
	}
	
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
		User newUser = new User(
				registerRequestDTO.name(), 
				registerRequestDTO.email(), 
				registerRequestDTO.operatorNumber(), 
				registerRequestDTO.password(), 
				registerRequestDTO.role(), 
				true
			);
		newUser = createNewUserUseCase.execute(newUser);
		
		UserDTO dto = new UserDTO(newUser.getId(), 
				newUser.getName(), 
				newUser.getEmail(), 
				newUser.getOperatorNumber(), 
				newUser.getRole(), 
				newUser.getActive(), 
				newUser.getLastLogin(), 
				newUser.getCreatedAt(), 
				newUser.getUpdatedAt()
			);
		
		return ResponseEntity.ok(dto);
	}
}