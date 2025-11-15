// Substitua em: com/example/BlogArtigos/artigos/ArtigosRequestDto.java
package com.example.BlogArtigos.artigos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArtigosRequestDto(
        @NotBlank String titulo, // <-- Renomeie de 'title' para 'titulo'
        @NotBlank String conteudo,
        @NotBlank String usuarioId,
        @NotNull Integer categoriaId
) {
}