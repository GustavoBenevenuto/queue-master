package com.benevenuto.queue_master.infra.config.secutiry;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.benevenuto.queue_master.domain.user.entity.User;

@Component
public class TokenService {
	@Value("${api.security.token.secret}")
	private String secret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
					.withIssuer("queue-master") // Define quem emitiu o token
					.withSubject(user.getEmail()) // Define o assunto do token, aqui o e-mail do usuário
					.withExpiresAt(generateExpirationDate()) // Define a data de expiração do token
					.sign(algorithm); // Assina o token com o algoritmo definido
            return token;
        } catch (JWTCreationException ex) {
            throw new RuntimeException("Error while generating token", ex);
        }
    }

    public String validateToken(String token) {
        try {
        	// Define novamente o algoritmo HMAC256 com o mesmo segredo
			Algorithm algorithm = Algorithm.HMAC256(secret);
			
			return JWT.require(algorithm)
					.withIssuer("queue-master") // Confirma que o token foi emitido por "SpringAuth"
					.build()
					.verify(token) // Verifica a validade do token
					.getSubject(); // Retorna o assunto do token, que é o e-mail do usuário
        } catch (JWTVerificationException ex) {
            return ""; // Token inválido
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
