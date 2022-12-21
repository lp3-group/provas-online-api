package com.example.demo.provas_online.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestaoDTO {
    private Integer id;
    private String texto;
    private Integer valor;
    private List<AlternativaDTO> alternativas;
}
