package com.example.demo.provas_online.api.controller;

import com.example.demo.provas_online.api.dto.CriarDisciplinaDTO;
import com.example.demo.provas_online.api.dto.DisciplinaDTO;
import com.example.demo.provas_online.exception.DisciplinaJaExisteException;
import com.example.demo.provas_online.model.entity.Disciplina;
import com.example.demo.provas_online.service.DisciplinaService;
import com.example.demo.provas_online.utility.MapeadorDeListas;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping()
    public ResponseEntity obterDisciplinas() {
        List<Disciplina> disciplinas = service.obterDisciplinas();

        List<DisciplinaDTO> corpoRetorno = MapeadorDeListas.mapeiaListaParaListaDeDTO(disciplinas, DisciplinaDTO.class);

        return ResponseEntity.ok(corpoRetorno);
    }
}
