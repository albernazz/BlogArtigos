
package com.example.BlogArtigos.vwartigospublicos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

/**
 * Esta é uma Entidade @Immutable (somente leitura) que mapeia
 * para a VIEW 'VW_ARTIGOS_PUBLICOS' do nosso banco de dados.
 */
@Entity
@Immutable // Diz ao Hibernate que esta entidade é somente leitura
@Table(name = "VW_ARTIGOS_PUBLICOS")
@Getter
public class VwArtigosPublicos {

    @Id
    @Column(name = "artigo_id")
    private String artigoId;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "conteudo")
    private String conteudo;

    @Column(name = "data_publicacao")
    private LocalDateTime dataPublicacao;

    @Column(name = "data_modificacao")
    private LocalDateTime dataModificacao;

    @Column(name = "autor_nome")
    private String autorNome; // Vem do JOIN com usuarios

    @Column(name = "categoria_nome")
    private String categoriaNome; // Vem do JOIN com categorias
}