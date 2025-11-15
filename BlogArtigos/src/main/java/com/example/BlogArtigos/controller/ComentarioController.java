
package com.example.BlogArtigos.controller;

import com.example.BlogArtigos.comentarios.Comentario;
import com.example.BlogArtigos.comentarios.ComentarioRepository;
import com.example.BlogArtigos.comentarios.ComentarioRequestDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioRepository repository;

    /**
     * Endpoint público para listar todos os comentários de um artigo específico.
     * Busca no MongoDB pelo ID do artigo (que é do MySQL).
     */
    @GetMapping("/{artigoId}")
    public ResponseEntity<List<Comentario>> getComentariosPorArtigo(@PathVariable String artigoId) {
        // Usa o método que criamos no repository
        List<Comentario> comentarios = repository.findByArtigoId(artigoId);
        return ResponseEntity.ok(comentarios);
    }

    /**
     * Endpoint público para qualquer um postar um novo comentário.
     */
    @PostMapping
    public ResponseEntity<Comentario> adicionarComentario(@RequestBody @Valid ComentarioRequestDto data) {

        // Converte o DTO para a Entidade Comentario
        Comentario novoComentario = new Comentario(
                data.artigoId(),
                data.autor(),
                data.texto()
        );

        // Salva o novo comentário no MongoDB
        Comentario comentarioSalvo = repository.save(novoComentario);

        // Retorna o comentário salvo (com ID e data de publicação)
        return ResponseEntity.status(201).body(comentarioSalvo);
    }
}