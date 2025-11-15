
package com.example.BlogArtigos.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.BlogArtigos.usuarios.Usuarios;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}") // Pega o segredo do application.properties
    private String secret;

    private static final String EMISSOR = "API BlogArtigos";

    // Método para GERAR o token
    public String gerarToken(Usuarios usuario) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(EMISSOR) // Quem está emitindo
                    .withSubject(usuario.getEmail()) // Quem é o "dono" do token
                    .withClaim("id", usuario.getId()) // Informação extra (payload)
                    .withExpiresAt(getExpirationDate()) // Data de expiração
                    .sign(algoritmo); // Assina
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    // Método para VALIDAR o token
    public String getSubject(String tokenJWT) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer(EMISSOR)
                    .build()
                    .verify(tokenJWT) // Verifica se é válido
                    .getSubject(); // Retorna o "dono" (email)
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    // Define a expiração do token (ex: 2 horas)
    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}