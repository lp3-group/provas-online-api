package com.example.demo.provas_online.api.controller;

import com.example.demo.provas_online.api.dto.NovaProvaDTO;
import com.example.demo.provas_online.api.dto.ProvaDTO;
import com.example.demo.provas_online.exception.DisciplinaNaoExisteException;
import com.example.demo.provas_online.exception.ProvaJaExisteException;
import com.example.demo.provas_online.model.entity.Prova;
import com.example.demo.provas_online.service.ProvaService;
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
@RequestMapping("/api/v1/provas")
@RequiredArgsConstructor
public class ProvaController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProvaService service;

    @PostMapping()
    public ResponseEntity criarProva(@RequestBody NovaProvaDTO corpoRequisicao) {
        try {
            Prova prova = modelMapper.map(corpoRequisicao, Prova.class);

            Prova provaCriada = service.validarECriarProva(prova);

            ProvaDTO retorno = modelMapper.map(provaCriada, ProvaDTO.class);

            return new ResponseEntity(retorno, HttpStatus.CREATED);
        } catch (DisciplinaNaoExisteException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ProvaJaExisteException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}