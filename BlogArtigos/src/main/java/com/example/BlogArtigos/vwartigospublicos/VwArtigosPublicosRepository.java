
package com.example.BlogArtigos.vwartigospublicos;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // Importar

// Repositório para a nossa View (VwArtigosPublicos)
public interface VwArtigosPublicosRepository extends JpaRepository<VwArtigosPublicos, String> {

    // --- MÉTODO NOVO ---
    /**
     * Busca na View por artigos cujo título contenha o termo de busca.
     * O Spring JPA cria a query: "WHERE titulo LIKE '%<termo>%'".
     * Isto irá utilizar o índice IDX_ARTIGO_TITULO que criou no seu Blog.sql [fonte: 1].
     */
    List<VwArtigosPublicos> findByTituloContainingIgnoreCase(String titulo);
}