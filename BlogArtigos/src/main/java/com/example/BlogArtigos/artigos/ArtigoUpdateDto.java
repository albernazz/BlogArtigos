
package com.example.BlogArtigos.artigos;

import jakarta.validation.constraints.NotBlank;

public record ArtigoUpdateDto(
        @NotBlank
        String titulo,

        @NotBlank
        String conteudo
) {
}