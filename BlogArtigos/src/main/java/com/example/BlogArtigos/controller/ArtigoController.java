// Substitua em: com/example/BlogArtigos/controller/ArtigoController.java
package com.example.BlogArtigos.controller;

import com.example.BlogArtigos.artigos.ArtigosRequestDto;
import com.example.BlogArtigos.artigos.ArtigosRepository;
import com.example.BlogArtigos.artigos.ArtigosResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("artigos")
public class ArtigoController {

    @Autowired
    private ArtigosRepository repository;

    @GetMapping
    public List<ArtigosResponseDto> getAll(){
        List<ArtigosResponseDto> ArtigosList = repository.findAll().stream().map(ArtigosResponseDto::new).toList();
        return ArtigosList;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<String> criarArtigo(@RequestBody @Valid ArtigosRequestDto data){

        String novoArtigoId = repository.callPublicarArtigo(
                data.usuarioId(),
                data.titulo(), // <-- Renomeie de 'data.title()' para 'data.titulo()'
                data.conteudo(),
                data.categoriaId()
        );

        return ResponseEntity.ok(novoArtigoId);
    }
}