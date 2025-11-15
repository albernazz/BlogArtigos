// Substitua em: com/example/BlogArtigos/gruposusuarios/GruposUsuariosRepository.java
package com.example.BlogArtigos.gruposusuarios;

import org.springframework.data.jpa.repository.JpaRepository;

// CORRIGIDO: de Long para Integer
public interface GruposUsuariosRepository extends JpaRepository<GruposUsuarios, Integer> {
}