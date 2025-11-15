package com.example.BlogArtigos.controller;

import com.example.BlogArtigos.artigos.ArtigosRequestDto;
import com.example.BlogArtigos.artigos.Artigos;
import com.example.BlogArtigos.artigos.ArtigosRepository;
import com.example.BlogArtigos.artigos.ArtigosResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("artigos")
public class ArtigoController {

    @Autowired
    private ArtigosRepository repository;

    @PostMapping
    public void criarArtigo(@RequestBody ArtigosRequestDto data){
        Artigos artigosData = new Artigos();
        repository.save(artigosData);
        return;
    }

    @GetMapping
    public List<ArtigosResponseDto> getAll(){
        List<ArtigosResponseDto> ArtigosList = repository.findAll().stream().map(ArtigosResponseDto::new).toList();
        return ArtigosList;
    }
}
