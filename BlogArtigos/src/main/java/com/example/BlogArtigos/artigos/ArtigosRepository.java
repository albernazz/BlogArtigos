// Substitua em: com/example/BlogArtigos/artigos/ArtigosRepository.java
package com.example.BlogArtigos.artigos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// 1. Corrigido de Long para String
public interface ArtigosRepository extends JpaRepository<Artigos, String> {

    // 2. Adicionada a chamada para a Stored Procedure do seu Blog.sql
    @Query(value = "CALL SP_PUBLICAR_ARTIGO(:p_usuario_id, :p_titulo, :p_conteudo, :p_categoria_id)",
            nativeQuery = true)
    String callPublicarArtigo(
            @Param("p_usuario_id") String usuarioId,
            @Param("p_titulo") String titulo,
            @Param("p_conteudo") String conteudo,
            @Param("p_categoria_id") Integer categoriaId
    );
}