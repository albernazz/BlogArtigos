
package com.example.BlogArtigos.controller;

import com.example.BlogArtigos.artigos.*;
import com.example.BlogArtigos.vwartigospublicos.VwArtigosPublicos;
import com.example.BlogArtigos.vwartigospublicos.VwArtigosPublicosRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("artigos")
public class ArtigoController {

    @Autowired
    private ArtigosRepository repository;

    @Autowired
    private VwArtigosPublicosRepository vwRepository;

    // --- CORREÇÃO APLICADA AQUI ---
    /**
     * GET /artigos
     * Lista todos os artigos (usando a View VW_ARTIGOS_PUBLICOS)
     * Agora aceita um parâmetro opcional '?busca=termo'
     */
    @GetMapping
    public ResponseEntity<List<VwArtigosPublicos>> getAll(
            // 1. Adiciona o parâmetro de busca opcional
            @RequestParam(name = "busca", required = false) String termoDeBusca
    ){
        List<VwArtigosPublicos> artigos;

        if (termoDeBusca == null || termoDeBusca.trim().isEmpty()) {
            // 2. Se não houver busca, retorna tudo
            artigos = vwRepository.findAll();
        } else {
            // 3. Se houver busca, usa o novo método do repositório
            //    Isto ativará o índice IDX_ARTIGO_TITULO
            artigos = vwRepository.findByTituloContainingIgnoreCase(termoDeBusca);
        }

        return ResponseEntity.ok(artigos);
    }
    // --- FIM DA CORREÇÃO ---

    /**
     * GET /artigos/{id}
     * Busca um artigo específico pela sua ID para edição.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Artigos> getArtigoById(@PathVariable String id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /artigos
     * Cria um novo artigo (usando a Procedure SP_PUBLICAR_ARTIGO)
     */
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

    /**
     * PUT /artigos/{id}
     * Atualiza um artigo existente (dispara as Triggers de UPDATE)
     */
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Artigos> atualizarArtigo(@PathVariable String id, @RequestBody @Valid ArtigoUpdateDto data) {
        Artigos artigo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artigo não encontrado")); // Simplificado

        artigo.setTitulo(data.titulo());
        artigo.setConteudo(data.conteudo());

        return ResponseEntity.ok(artigo);
    }
}