// Substitua em: com/example/BlogArtigos/controller/ArtigoController.java
package com.example.BlogArtigos.controller;

import com.example.BlogArtigos.artigos.ArtigosRequestDto;
import com.example.BlogArtigos.artigos.ArtigosRepository;
import com.example.BlogArtigos.artigos.ArtigosResponseDto;
import jakarta.validation.Valid; // Importar
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Importar
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("artigos")
public class ArtigoController {

    @Autowired
    private ArtigosRepository repository;

    // O GET (leitura) pode usar o findAll() padrão do JPA
    @GetMapping
    public List<ArtigosResponseDto> getAll(){
        List<ArtigosResponseDto> ArtigosList = repository.findAll().stream().map(ArtigosResponseDto::new).toList();
        return ArtigosList;
    }

    // O POST (criação) DEVE usar a Stored Procedure
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<String> criarArtigo(@RequestBody @Valid ArtigosRequestDto data){

        // Chama a Stored Procedure definida no Repository
        String novoArtigoId = repository.callPublicarArtigo(
                data.usuarioId(),
                data.title(),
                data.texto(),
                data.categoriaId()
        );

        // Retorna o novo ID criado pela procedure
        return ResponseEntity.ok(novoArtigoId);
    }
}