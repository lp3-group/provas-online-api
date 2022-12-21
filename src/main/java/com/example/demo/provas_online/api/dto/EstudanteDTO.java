package com.example.demo.provas_online.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudanteDTO {
    private Integer id;
    private String nome;
    private String sobrenome;
    private boolean primeiroAcesso;
    private String nomeUsuario;
    private String matricula;
}
