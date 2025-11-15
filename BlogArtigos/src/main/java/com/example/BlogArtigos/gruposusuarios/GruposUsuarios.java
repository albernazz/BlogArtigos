
package com.example.BlogArtigos.gruposusuarios;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "grupos_usuarios")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GruposUsuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_grupo", unique = true, nullable = false)
    private String nomeGrupo;

    // Você pode adicionar um construtor se precisar criar grupos
    public GruposUsuarios(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }
}