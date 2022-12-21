package com.example.demo.provas_online.api.dto;

import com.example.demo.provas_online.types.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRetornoDTO {
    private Integer id;
    private String nome;
    private String sobrenome;
    private String nomeUsuario;
    private String token;
    private TipoUsuario tipoUsuario;
}
