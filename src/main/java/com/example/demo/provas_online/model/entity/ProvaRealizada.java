package com.example.demo.provas_online.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProvaRealizada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    private Date realizadaEm;

    @ManyToOne
    @JoinColumn(name = "estudante_id")
    private Estudante estudante;

    @ManyToOne
    @JoinColumn(name = "prova_id")
    private Prova prova;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "alternativas_marcadas",
        joinColumns = @JoinColumn(name = "alternativa_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "prova_realizada_id", referencedColumnName = "id"))
    private List<Alternativa> alternativasMarcadas;
}
