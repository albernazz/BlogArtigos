// Substitua em: com/example/BlogArtigos/artigos/ArtigosResponseDto.java
package com.example.BlogArtigos.artigos;

import com.example.BlogArtigos.usuarios.Usuarios;

// 1. Renomeie aqui
public record ArtigosResponseDto(String id, String titulo, String conteudo, Usuarios usuario) {

    public ArtigosResponseDto (Artigos artigos){
        // 2. Renomeie o método getter (Lombok cria getTitulo() agora)
        this(artigos.getId(), artigos.getTitulo(), artigos.getConteudo(), artigos.getUsuario());
    }
}