package com.example.demo.provas_online.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProvaRealizadaDTO {
    private Integer id;
    private Date realizadaEm;
    private double nota;
    private int respostasCertas;
    private List<QuestaoRealizadaDTO> questoes;
}
