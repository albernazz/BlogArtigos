// Adicione este método DENTRO do PageController.java

package com.example.BlogArtigos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {

    @GetMapping
    public String getLoginPage() {
        return "login"; // Serve o login.html
    }

    @GetMapping("/home")
    public String getHomePage() {
        return "home"; // Serve o home.html
    }

    // --- ADICIONE ESTE MÉTODO ---
    /**
     * Mapeia a URL /stats
     * @return O nome do arquivo 'estatisticas.html'
     */
    @GetMapping("/stats")
    public String getStatsPage() {
        return "estatisticas"; // Serve o estatisticas.html
    }
    // --- FIM DA ADIÇÃO ---
}