package com.example.demo.provas_online.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlterarSenhaDTO {
    private String senha;
    private String confirmaSenha;
}
