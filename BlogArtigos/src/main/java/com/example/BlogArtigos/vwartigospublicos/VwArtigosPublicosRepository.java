
package com.example.BlogArtigos.vwartigospublicos;

import com.example.BlogArtigos.vwartigospublicos.VwArtigosPublicos;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositório para a nossa View (VwArtigosPublicos)
public interface VwArtigosPublicosRepository extends JpaRepository<VwArtigosPublicos, String> {
}