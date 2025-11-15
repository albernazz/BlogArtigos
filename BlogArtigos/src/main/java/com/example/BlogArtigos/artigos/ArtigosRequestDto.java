
package com.example.BlogArtigos.artigos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArtigosRequestDto(
        @NotBlank String title,
        @NotBlank String texto,
        @NotBlank String usuarioId,
        @NotNull Integer categoriaId
) {
}