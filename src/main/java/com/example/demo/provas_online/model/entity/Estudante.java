package com.example.demo.provas_online.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Estudante extends Usuario {
    @Column(unique = true)
    private String matricula;
}
