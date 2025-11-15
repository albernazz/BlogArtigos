// Substitua em: com/example/BlogArtigos/usuarios/UsuariosRepository.java
package com.example.BlogArtigos.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Importar
import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuarios, String> {

    // Este método você já tinha
    Optional<Usuarios> findByEmail(String email);

    // Adicionamos a chamada nativa para sua função FN_GERA_ID_USUARIO
    @Query(value = "SELECT FN_GERA_ID_USUARIO()", nativeQuery = true)
    String callGeraIdUsuario();
}