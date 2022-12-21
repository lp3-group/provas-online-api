package com.example.demo.provas_online.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlternativaDTO {
    private Integer id;
    private String texto;
    private boolean respostaCerta;
}
