package com.example.BlogArtigos.artigos;

import com.example.BlogArtigos.usuarios.Usuarios;

public record ArtigosResponseDto(Long id, String title, String texto, Usuarios usuario) {
    public ArtigosResponseDto (Artigos artigos){
        this(artigos.getId(), artigos.getTitle(), artigos.getTexto(), artigos.getUsuario());
    }
}
