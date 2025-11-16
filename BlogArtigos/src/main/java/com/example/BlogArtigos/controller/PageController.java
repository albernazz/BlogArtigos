// Substitua em: com/example/BlogArtigos/controller/PageController.java
package com.example.BlogArtigos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/editar/{id}")
    public String getEditarPage(@PathVariable String id, Model model) {
        model.addAttribute("artigoId", id);
        return "editar";
    }

    // --- MÉTODO NOVO ---
    /**
     * Mapeia a URL /registrar
     * @return O nome do arquivo 'registrar.html'
     */
    @GetMapping("/registrar")
    public String getRegistroPage() {
        return "registrar"; // Serve o registrar.html
    }
    // --- FIM DO NOVO ---
}