
package com.example.BlogArtigos.comentarios;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

// Usamos MongoRepository em vez de JpaRepository
public interface ComentarioRepository extends MongoRepository<Comentario, String> {

    // O Spring Data MongoDB vai criar esta query automaticamente:
    // "db.comentarios.find({ 'artigoId': ? })"
    List<Comentario> findByArtigoId(String artigoId);
}