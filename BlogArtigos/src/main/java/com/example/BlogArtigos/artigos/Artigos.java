// Substitua em: com/example/BlogArtigos/artigos/Artigos.java
package com.example.BlogArtigos.artigos;

import com.example.BlogArtigos.usuarios.Usuarios;
import jakarta.persistence.*;
import lombok.*; // Importar Setter

@Table(name = "artigos")
@Entity(name = "artigos")
@Getter
@Setter // <-- CORREÇÃO AQUI: Permite a alteração dos campos
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Artigos {

    @Id
    private String id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "conteudo", columnDefinition = "TEXT")
    private String conteudo;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuario;
}