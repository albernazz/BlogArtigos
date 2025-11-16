// Substitua em: com/example/BlogArtigos/controller/ComentarioController.java
package com.example.BlogArtigos.controller;

import com.example.BlogArtigos.comentarios.Comentario;
import com.example.BlogArtigos.comentarios.ComentarioRepository;
import com.example.BlogArtigos.comentarios.ComentarioRequestDto;
import com.example.BlogArtigos.usuarios.Usuarios; // Importar
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Importar
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioRepository repository;

    /**
     * Endpoint público para listar comentários
     */
    @GetMapping("/{artigoId}")
    public ResponseEntity<List<Comentario>> getComentariosPorArtigo(@PathVariable String artigoId) {
        List<Comentario> comentarios = repository.findByArtigoId(artigoId);
        return ResponseEntity.ok(comentarios);
    }

    // --- MÉTODO MODIFICADO ---
    /**
     * Endpoint protegido para postar um novo comentário.
     * O nome do autor é pego automaticamente do utilizador logado.
     */
    @PostMapping
    public ResponseEntity<Comentario> adicionarComentario(
            @RequestBody @Valid ComentarioRequestDto data,
            @AuthenticationPrincipal Usuarios usuarioLogado // 1. Recebe o utilizador logado
    ) {

        // 2. Converte o DTO (que não tem 'autor') para a Entidade
        Comentario novoComentario = new Comentario(
                data.artigoId(),
                usuarioLogado.getNome(), // 3. Usa o nome do utilizador do token
                data.texto()
        );

        Comentario comentarioSalvo = repository.save(novoComentario);

        return ResponseEntity.status(201).body(comentarioSalvo);
    }
}