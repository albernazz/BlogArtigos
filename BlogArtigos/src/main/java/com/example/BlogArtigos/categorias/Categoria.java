
package com.example.BlogArtigos.categorias;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "categorias")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Usando Integer para bater com o 'INT' do seu SQL

    @Column(unique = true, nullable = false)
    private String nome;
}