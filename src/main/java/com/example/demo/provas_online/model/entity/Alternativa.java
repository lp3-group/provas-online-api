package com.example.demo.provas_online.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Alternativa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String texto;

    private boolean respostaCerta;

    @ManyToOne
    @JoinColumn(name = "questao_id")
    private Questao questao;
}
