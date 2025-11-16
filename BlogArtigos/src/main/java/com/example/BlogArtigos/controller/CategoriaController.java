
package com.example.BlogArtigos.controller;

import com.example.BlogArtigos.categorias.Categoria;
import com.example.BlogArtigos.categorias.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository repository;

    /**
     * Endpoint público para listar todas as categorias
     */
    @GetMapping
    public ResponseEntity<List<Categoria>> getAllCategorias() {
        List<Categoria> categorias = repository.findAll();
        return ResponseEntity.ok(categorias);
    }
}