package com.example.demo.provas_online.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NovaProvaDTO {
    private String titulo;
    private List<NovaQuestaoDTO> questoes;
    private Integer idDisciplina;
}
