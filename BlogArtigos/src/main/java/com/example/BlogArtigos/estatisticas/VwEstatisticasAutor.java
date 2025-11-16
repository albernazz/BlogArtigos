
package com.example.BlogArtigos.estatisticas;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

/**
 * Entidade @Immutable (somente leitura) que mapeia
 * para a VIEW 'VW_ESTATISTICAS_AUTOR' do nosso banco.
 */
@Entity
@Immutable // Somente leitura
@Table(name = "VW_ESTATISTICAS_AUTOR")
@Getter
public class VwEstatisticasAutor {

    @Id
    @Column(name = "usuario_id")
    private String usuarioId;

    @Column(name = "autor_nome")
    private String autorNome;

    @Column(name = "email")
    private String email;

    @Column(name = "nome_grupo")
    private String nomeGrupo;

    @Column(name = "total_artigos")
    private Integer totalArtigos; // Sua função FN_CONTA_ARTIGOS_USUARIO retorna INT
}