package com.example.demo.provas_online.api.dto;

import com.example.demo.provas_online.types.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriarUsuarioDTO {
    private String nome;
    private String sobrenome;
    private String senha;
    private TipoUsuario tipoUsuario;
    private String nomeUsuario;
    private String matricula;
}
