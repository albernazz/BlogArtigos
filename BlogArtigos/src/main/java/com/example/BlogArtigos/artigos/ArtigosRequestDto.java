package com.example.BlogArtigos.artigos;

import com.example.BlogArtigos.usuarios.Usuarios;

public record ArtigosRequestDto(String title, String texto, Usuarios usuario) {
}
