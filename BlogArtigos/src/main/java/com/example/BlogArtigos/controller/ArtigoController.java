// Substitua em: com/example/BlogArtigos/controller/ArtigoController.java
package com.example.BlogArtigos.controller;

import com.example.BlogArtigos.artigos.*; // Importa tudo (inclusive a nova View)
import com.example.BlogArtigos.vwartigospublicos.VwArtigosPublicos;
import com.example.BlogArtigos.vwartigospublicos.VwArtigosPublicosRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("artigos")
public class ArtigoController {

    @Autowired
    private ArtigosRepository repository; // Ainda usado para o POST

    // --- CORREÇÃO AQUI ---
    @Autowired
    private VwArtigosPublicosRepository vwRepository; // 1. Injeta o novo repo da VIEW

    /**
     * Agora usa a VIEW (VwArtigosPublicos) para listar os artigos.
     * Isso cumpre o requisito do trabalho e já inclui o nome do autor e da categoria.
     */
    @GetMapping
    public ResponseEntity<List<VwArtigosPublicos>> getAll(){ // 2. Retorna a lista da View
        // 3. Usa o findAll() do repositório da View
        return ResponseEntity.ok(vwRepository.findAll());
    }
    // --- FIM DA CORREÇÃO ---


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<String> criarArtigo(@RequestBody @Valid ArtigosRequestDto data){

        String novoArtigoId = repository.callPublicarArtigo(
                data.usuarioId(),
                data.titulo(),
                data.conteudo(),
                data.categoriaId()
        );

        return ResponseEntity.ok(novoArtigoId);
    }
}