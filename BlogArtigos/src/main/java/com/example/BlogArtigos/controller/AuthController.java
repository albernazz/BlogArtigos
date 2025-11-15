package com.example.BlogArtigos.controller;

import com.example.BlogArtigos.gruposusuarios.GruposUsuariosRepository; // Vai precisar ser criado
import com.example.BlogArtigos.security.DtoAuth;
import com.example.BlogArtigos.security.DtoToken;
import com.example.BlogArtigos.security.TokenService;
import com.example.BlogArtigos.usuarios.Usuarios;
import com.example.BlogArtigos.usuarios.UsuariosRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping // Deixamos o mapping principal em branco
public class AuthController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injeta o BCrypt

    @Autowired
    private GruposUsuariosRepository gruposUsuariosRepository; // Injeta o novo repository

    /**
     * Endpoint público para login.
     * Recebe email e senha, devolve um token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<DtoToken> efetuarLogin(@RequestBody @Valid DtoAuth data) {

        // 1. Cria o token de autenticação (ainda não validado)
        var authToken = new UsernamePasswordAuthenticationToken(data.email(), data.senha());

        // 2. O Spring Security valida o usuário e a senha (usando o BCrypt)
        var authentication = manager.authenticate(authToken);

        // 3. Se deu certo, gera o token JWT
        var usuario = (Usuarios) authentication.getPrincipal();
        var tokenJWT = tokenService.gerarToken(usuario);

        // 4. Devolve o token
        return ResponseEntity.ok(new DtoToken(tokenJWT));
    }

    /**
     * Endpoint temporário para registrar um novo usuário (AUTOR).
     * Essencial para cadastrar a primeira senha criptografada (BCrypt).
     */
    @PostMapping("/registrar")
    public ResponseEntity<String> registrarNovoUsuario(@RequestBody @Valid DtoAuth data) {

        if (usuariosRepository.findByEmail(data.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já está em uso.");
        }

        // --- CORREÇÃO AQUI ---
        // Mude de 2L (Long) para 2 (int, que vira Integer)
        var grupoAutor = gruposUsuariosRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Grupo 'AUTOR' (ID 2) não foi encontrado no banco!"));
        // --- FIM DA CORREÇÃO ---

        var senhaCriptografada = passwordEncoder.encode(data.senha());
        String novoId = usuariosRepository.callGeraIdUsuario();

        Usuarios novoUsuario = new Usuarios(
                novoId,
                "Autor " + data.email().split("@")[0],
                data.email(),
                senhaCriptografada,
                grupoAutor
        );

        usuariosRepository.save(novoUsuario);

        return ResponseEntity.ok("Usuário 'AUTOR' registrado com sucesso! ID: " + novoId);
    }
}