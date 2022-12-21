package com.example.demo.provas_online.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NovaQuestaoDTO {
    private String texto;
    private Integer valor;
    private List<NovaAlternativaDTO> alternativas;
}
