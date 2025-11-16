// Substitua em: com/example/BlogArtigos/controller/ArtigoController.java
package com.example.BlogArtigos.controller;

import com.example.BlogArtigos.artigos.*;
import com.example.BlogArtigos.comentarios.ComentarioRepository; // Importar
import com.example.BlogArtigos.usuarios.Usuarios; // Importar
import com.example.BlogArtigos.vwartigospublicos.VwArtigosPublicos;
import com.example.BlogArtigos.vwartigospublicos.VwArtigosPublicosRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Importar
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Importar
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

    @Autowired
    private ComentarioRepository comentarioRepository; // 1. Injetar o repo de comentários

    // ... (Métodos GET /artigos e GET /artigos/{id} continuam iguais) ...
    @GetMapping
    public ResponseEntity<List<VwArtigosPublicos>> getAll(@RequestParam(name = "busca", required = false) String termoDeBusca){
        List<VwArtigosPublicos> artigos;
        if (termoDeBusca == null || termoDeBusca.trim().isEmpty()) {
            artigos = vwRepository.findAll();
        } else {
            artigos = vwRepository.findByTituloContainingIgnoreCase(termoDeBusca);
        }
        return ResponseEntity.ok(artigos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artigos> getArtigoById(@PathVariable String id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ... (Método POST /artigos continua igual) ...
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

    // ... (Método PUT /artigos/{id} continua igual) ...
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Artigos> atualizarArtigo(@PathVariable String id, @RequestBody @Valid ArtigoUpdateDto data) {
        Artigos artigo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artigo não encontrado"));

        artigo.setTitulo(data.titulo());
        artigo.setConteudo(data.conteudo());

        return ResponseEntity.ok(artigo);
    }

    // --- MÉTODO NOVO ---
    /**
     * DELETE /artigos/{id}
     * Apaga um artigo e TODOS os seus comentários (no MongoDB).
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deletarArtigo(
            @PathVariable String id,
            @AuthenticationPrincipal Usuarios usuarioLogado
    ) {
        // 1. Encontra o artigo
        Artigos artigo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artigo não encontrado"));

        // 2. VERIFICAÇÃO DE PERMISSÃO (Dono ou Admin)
        boolean isAdmin = usuarioLogado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isDono = artigo.getUsuario().getId().equals(usuarioLogado.getId());

        if (!isAdmin && !isDono) {
            // Retorna 403 Forbidden (Acesso Negado)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // 3. Apaga os comentários do MongoDB
        comentarioRepository.deleteByArtigoId(id);

        // 4. Apaga o artigo do MySQL
        // (O ON DELETE CASCADE do seu SQL [fonte: 1] irá apagar as entradas de 'artigo_categoria')
        repository.delete(artigo);

        // Retorna 204 No Content (Sucesso, sem corpo)
        return ResponseEntity.noContent().build();
    }
}