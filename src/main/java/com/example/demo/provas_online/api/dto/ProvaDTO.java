package com.example.demo.provas_online.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvaDTO {
    private Integer id;
    private Date criadaEm;
    private List<QuestaoDTO> questoes;
    private DisciplinaDTO disciplina;
}
