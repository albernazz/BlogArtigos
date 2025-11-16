
package com.example.BlogArtigos.comentarios;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ComentarioRepository extends MongoRepository<Comentario, String> {

    List<Comentario> findByArtigoId(String artigoId);

    // --- MÉTODO NOVO ---
    // Cria um 'DELETE FROM comentarios WHERE artigoId = ?'
    void deleteByArtigoId(String artigoId);
    // --- FIM DO MÉTODO NOVO ---
}