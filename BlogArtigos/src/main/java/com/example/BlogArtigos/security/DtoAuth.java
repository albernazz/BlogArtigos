
package com.example.BlogArtigos.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// DTO para o JSON do Login
public record DtoAuth(
        @NotBlank @Email
        String email,

        @NotBlank
        String senha
) {}