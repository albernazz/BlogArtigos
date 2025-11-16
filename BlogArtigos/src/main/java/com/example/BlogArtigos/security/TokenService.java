
package com.example.BlogArtigos.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.BlogArtigos.usuarios.Usuarios;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority; // Importar
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors; // Importar

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private static final String EMISSOR = "API BlogArtigos";

    // --- MÉTODO MODIFICADO ---
    public String gerarToken(Usuarios usuario) {
        try {
            // 1. Pega a lista de 'authorities' (ex: "AUTOR")
            var roles = usuario.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(EMISSOR)
                    .withSubject(usuario.getEmail())
                    .withClaim("id", usuario.getId())
                    // 2. Adiciona as roles (funções) ao token
                    .withClaim("scope", roles)
                    .withExpiresAt(getExpirationDate())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }
    // --- FIM DA MODIFICAÇÃO ---

    public String getSubject(String tokenJWT) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer(EMISSOR)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}