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

    // --- ADICIONE ESTE MÉTODO ---
    /**
     * Mapeia a URL /home
     * @return O nome do arquivo 'home.html'
     */
    @GetMapping("/home")
    public String getHomePage() {
        return "home"; // O Spring procura por 'src/main/resources/templates/home.html'
    }
    // --- FIM DA ADIÇÃO ---
}