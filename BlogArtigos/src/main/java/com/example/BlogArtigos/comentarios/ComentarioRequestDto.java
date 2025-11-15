
package com.example.BlogArtigos.comentarios;

import jakarta.validation.constraints.NotBlank;

public record ComentarioRequestDto(
        @NotBlank
        String artigoId, // ID do Artigo do MySQL

        @NotBlank
        String autor,

        @NotBlank
        String texto
) {
}