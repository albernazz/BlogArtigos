
package com.example.BlogArtigos.comentarios;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Document(collection = "comentarios") // Define o nome da "tabela" (collection) no MongoDB
@Getter
@Setter
@NoArgsConstructor
public class Comentario {

    @Id
    private String id; // O ID do MongoDB é uma String (ex: "60c72b...f")

    // O ID do artigo (MySQL) ao qual este comentário pertence
    // Vamos usar isso para buscar os comentários
    private String artigoId;

    private String autor; // Nome de quem comentou (pode ser "Anônimo")
    private String texto;
    private LocalDateTime dataPublicacao = LocalDateTime.now();

    public Comentario(String artigoId, String autor, String texto) {
        this.artigoId = artigoId;
        this.autor = autor;
        this.texto = texto;
    }
}