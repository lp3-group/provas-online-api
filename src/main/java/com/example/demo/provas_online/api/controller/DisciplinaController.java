package com.example.demo.provas_online.api.controller;

import com.example.demo.provas_online.api.dto.CriarDisciplinaDTO;
import com.example.demo.provas_online.exception.DisciplinaJaExisteException;
import com.example.demo.provas_online.model.entity.Disciplina;
import com.example.demo.provas_online.service.DisciplinaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/disciplinas")
@RequiredArgsConstructor
public class DisciplinaController {
    @Autowired
    private ModelMapper modelMapper;

    private final DisciplinaService service;

    @PostMapping()
    public ResponseEntity criarDisciplina(@RequestBody CriarDisciplinaDTO corpoRequisicao) {
        try {
            Disciplina disciplina = modelMapper.map(corpoRequisicao, Disciplina.class);

            service.validarECriarDisciplina(disciplina);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DisciplinaJaExisteException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
