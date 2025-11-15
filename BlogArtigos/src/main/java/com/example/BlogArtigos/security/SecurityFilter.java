
package com.example.BlogArtigos.security;

import com.example.BlogArtigos.usuarios.UsuariosRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Anota como um componente do Spring
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);

        // Se houver um token válido
        if (token != null) {
            // Valida o token e pega o email (subject)
            var email = tokenService.getSubject(token);

            // Busca o usuário no banco
            UserDetails usuario = usuariosRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado no filtro de segurança"));

            // Cria a autenticação para o Spring
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

            // Coloca o usuário no "contexto" de segurança da requisição
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continua a requisição
        filterChain.doFilter(request, response);
    }

    // Método para extrair o token do Header (ex: "Bearer <token>")
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        // Remove o "Bearer "
        return authHeader.replace("Bearer ", "");
    }
}