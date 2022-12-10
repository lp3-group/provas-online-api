package com.example.demo.provas_online.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Prova {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    private Date criadaEm;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prova")
    private List<Questao> questoes;

    @ManyToOne
    @JoinColumn(name = "disciplina_id")
    private Disciplina disciplina;
}
