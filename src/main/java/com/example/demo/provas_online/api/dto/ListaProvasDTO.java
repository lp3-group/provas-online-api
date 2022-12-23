package com.example.demo.provas_online.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListaProvasDTO {
    private Integer id;
    private String titulo;
    private Date criadaEm;
    private DisciplinaDTO disciplina;
}
