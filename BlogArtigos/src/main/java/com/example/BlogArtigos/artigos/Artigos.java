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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String texto;
    private Usuarios usuario;

    public Artigos(ArtigosRequestDto data){
        this.title = data.title();
        this.texto = data.texto();
        this.usuario = data.usuario();
    }
}
