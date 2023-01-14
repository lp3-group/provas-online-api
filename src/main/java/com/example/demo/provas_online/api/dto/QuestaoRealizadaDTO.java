package com.example.demo.provas_online.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestaoRealizadaDTO {
    private Integer id;
    private String texto;
    private double valor;
    private AlternativaDTO alternativaMarcada;
}
