
package com.example.BlogArtigos.controller;

import com.example.BlogArtigos.estatisticas.VwEstatisticasAutor;
import com.example.BlogArtigos.estatisticas.VwEstatisticasAutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/estatisticas")
public class EstatisticasController {

    @Autowired
    private VwEstatisticasAutorRepository repository;

    /**
     * Endpoint para buscar os dados da view de estatísticas.
     */
    @GetMapping
    public ResponseEntity<List<VwEstatisticasAutor>> getEstatisticas() {
        return ResponseEntity.ok(repository.findAll());
    }
}