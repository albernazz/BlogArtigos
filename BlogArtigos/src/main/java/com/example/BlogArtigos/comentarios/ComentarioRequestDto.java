
package com.example.BlogArtigos.comentarios;

import jakarta.validation.constraints.NotBlank;

// O 'autor' foi removido. Só precisamos do artigo e do texto.
public record ComentarioRequestDto(
        @NotBlank
        String artigoId, // ID do Artigo do MySQL

        @NotBlank
        String texto
) {
}