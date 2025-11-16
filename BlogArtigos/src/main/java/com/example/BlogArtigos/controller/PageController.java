// Substitua em: com/example/BlogArtigos/controller/PageController.java
package com.example.BlogArtigos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Importar
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // Importar
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {

    @GetMapping
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/home")
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/stats")
    public String getStatsPage() {
        return "estatisticas";
    }

    // --- ENDPOINT NOVO ---
    /**
     * Mapeia a URL /editar/{id}
     * Passa o 'id' do artigo para o modelo do Thymeleaf,
     * para que o JavaScript na página 'editar.html' possa lê-lo.
     */
    @GetMapping("/editar/{id}")
    public String getEditarPage(@PathVariable String id, Model model) {
        model.addAttribute("artigoId", id); // Passa o ID para o HTML
        return "editar"; // Serve o editar.html
    }
    // --- FIM DO NOVO ---
}