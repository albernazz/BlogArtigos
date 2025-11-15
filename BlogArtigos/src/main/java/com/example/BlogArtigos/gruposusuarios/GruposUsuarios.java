// Substitua em: com/example/BlogArtigos/gruposusuarios/GruposUsuarios.java
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
    private Integer id; // <-- CORRIGIDO: de Long para Integer

    @Column(name = "nome_grupo", unique = true, nullable = false)
    private String nomeGrupo;

    public GruposUsuarios(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }
}