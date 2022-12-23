package com.example.demo.provas_online.api.controller;

import com.example.demo.provas_online.api.dto.ListaProvasDTO;
import com.example.demo.provas_online.api.dto.NovaProvaDTO;
import com.example.demo.provas_online.api.dto.ProvaDTO;
import com.example.demo.provas_online.exception.*;
import com.example.demo.provas_online.model.entity.Prova;
import com.example.demo.provas_online.service.ProvaService;
import com.example.demo.provas_online.utility.MapeadorDeListas;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        } catch (QuestaoInvalidaException | AlternativaInvalidaException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping()
    public ResponseEntity listarProvas() {
        List<Prova> provas = service.obterProvas();

        List<ListaProvasDTO> retorno = MapeadorDeListas.mapeiaListaParaListaDeDTO(provas, ListaProvasDTO.class);

        return ResponseEntity.ok(retorno);
    }

    @GetMapping("/{id}")
    public ResponseEntity obterProvaPeloId(@PathVariable("id") Integer id) {
        try {
            Prova prova = service.validarEObterProvaPeloId(id);

            ProvaDTO retorno = modelMapper.map(prova, ProvaDTO.class);

            return ResponseEntity.ok(retorno);
        } catch (ProvaNaoExisteException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizarProva(@PathVariable("id") Integer id, @RequestBody NovaProvaDTO corpoRequisicao) {
        try {
            Prova prova = modelMapper.map(corpoRequisicao, Prova.class);
            prova.setId(id);

            Prova provaEditada = service.validarEAtualizarProva(prova);

            ProvaDTO retorno = modelMapper.map(provaEditada, ProvaDTO.class);

            return ResponseEntity.ok(retorno);
        } catch (ProvaNaoExisteException | DisciplinaNaoExisteException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ProvaJaExisteException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        } catch (QuestaoInvalidaException | AlternativaInvalidaException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluirProva(@PathVariable("id") Integer id) {
        try {
            service.validarEExcluirProva(id);

            return ResponseEntity.noContent().build();
        } catch (ProvaNaoExisteException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
