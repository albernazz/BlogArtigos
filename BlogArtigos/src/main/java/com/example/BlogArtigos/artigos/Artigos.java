// Substitua em: com/example/BlogArtigos/artigos/Artigos.java
package com.example.BlogArtigos.artigos;

import com.example.BlogArtigos.usuarios.Usuarios;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "artigos")
@Entity(name = "artigos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Artigos {

    @Id
    private String id;

    // --- CORREÇÃO AQUI ---
    @Column(name = "titulo") // 1. Mude o nome da coluna
    private String titulo; // 2. Renomeie o campo
    // --- FIM DA CORREÇÃO ---

    @Column(name = "conteudo", columnDefinition = "TEXT")
    private String conteudo;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuario;
}