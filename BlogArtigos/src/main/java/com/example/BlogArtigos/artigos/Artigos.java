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
    private String title;
    @Column(columnDefinition = "TEXT")
    private String texto;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuario;


}