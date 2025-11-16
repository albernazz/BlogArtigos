
package com.example.BlogArtigos.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull; // Importar
import jakarta.validation.constraints.Size;

public record DtoRegistro(
        @NotBlank
        String nome,

        @NotBlank @Email
        String email,

        @NotBlank @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String senha,

        // --- CAMPO NOVO ---
        @NotNull(message = "Você deve selecionar um tipo de conta")
        Integer grupoId // Irá receber 2 para AUTOR ou 3 para LEITOR
) {}