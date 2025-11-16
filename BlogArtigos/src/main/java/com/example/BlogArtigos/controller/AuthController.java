
package com.example.BlogArtigos.controller;

import com.example.BlogArtigos.gruposusuarios.GruposUsuariosRepository;
import com.example.BlogArtigos.security.DtoAuth;
import com.example.BlogArtigos.security.DtoRegistro;
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
@RequestMapping
public class AuthController {

    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UsuariosRepository usuariosRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GruposUsuariosRepository gruposUsuariosRepository;

    @PostMapping("/login")
    public ResponseEntity<DtoToken> efetuarLogin(@RequestBody @Valid DtoAuth data) {
        var authToken = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var authentication = manager.authenticate(authToken);
        var usuario = (Usuarios) authentication.getPrincipal();
        var tokenJWT = tokenService.gerarToken(usuario);
        return ResponseEntity.ok(new DtoToken(tokenJWT));
    }

    // --- MÉTODO MODIFICADO ---
    @PostMapping("/registrar")
    public ResponseEntity<String> registrarNovoUsuario(@RequestBody @Valid DtoRegistro data) {

        if (usuariosRepository.findByEmail(data.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já está em uso.");
        }

        // --- CORREÇÃO AQUI ---
        // 1. Validação de segurança: Não permitir que ninguém se registe como ADMIN (ID 1)
        if (data.grupoId() == 1) {
            return ResponseEntity.badRequest().body("Tipo de conta inválido.");
        }

        // 2. Busca o grupo dinamicamente (2 para AUTOR, 3 para LEITOR)
        var grupoSelecionado = gruposUsuariosRepository.findById(data.grupoId())
                .orElseThrow(() -> new RuntimeException("Grupo selecionado (ID " + data.grupoId() + ") não foi encontrado!"));

        var senhaCriptografada = passwordEncoder.encode(data.senha());
        String novoId = usuariosRepository.callGeraIdUsuario();

        Usuarios novoUsuario = new Usuarios(
                novoId,
                data.nome(),
                data.email(),
                senhaCriptografada,
                grupoSelecionado // <-- Salva com o grupo selecionado
        );

        usuariosRepository.save(novoUsuario);

        return ResponseEntity.ok("Usuário registrado com sucesso! ID: " + novoId);
    }
}