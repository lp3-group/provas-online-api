package com.example.demo.provas_online.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Questao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String texto;

    private Double valor = 0.0;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questao")
    private List<Alternativa> alternativas;

    @ManyToOne
    @JoinColumn(name = "prova_id")
    private Prova prova;
}
