package com.example.demo.provas_online.api.controller;

import com.example.demo.provas_online.api.dto.*;
import com.example.demo.provas_online.exception.*;
import com.example.demo.provas_online.model.entity.*;
import com.example.demo.provas_online.service.AlternativaService;
import com.example.demo.provas_online.service.ProvaService;
import com.example.demo.provas_online.service.UsuarioService;
import com.example.demo.provas_online.utility.MapeadorDeListas;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/provas")
@RequiredArgsConstructor
public class ProvaController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProvaService service;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AlternativaService alternativaService;

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

    @GetMapping("/{id}/realizar")
    public ResponseEntity pegarProvaParaRealizar(@PathVariable("id") Integer id) {
        try {
            Prova prova = service.validarEObterProvaRandomizada(id);

            ProvaDTO retorno = modelMapper.map(prova, ProvaDTO.class);

            return ResponseEntity.ok(retorno);
        } catch (ProvaNaoExisteException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/realizar")
    public ResponseEntity realizarProva(
            @PathVariable("id") Integer id,
            @RequestBody List<IdAlternativaDTO> idsAlternativasMarcadas,
            Authentication authentication
    ) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Estudante estudante = (Estudante) usuarioService.getUsuario(userDetails.getUsername()).get();

            Prova prova = service.validarEObterProvaPeloId(id);

            List<Integer> idsAlternativas = idsAlternativasMarcadas.stream().map(IdAlternativaDTO::getId)
                    .toList();

            ProvaRealizada provaRealizada = service.realizarProva(estudante, prova, idsAlternativas);
            double nota = service.calcularNota(provaRealizada.getAlternativasMarcadas());
            int quantidadeQuestoes = service.filtrarQuestoesCertas(provaRealizada.getAlternativasMarcadas()).size();

            List<QuestaoRealizadaDTO> questoesRespondidas = provaRealizada.getAlternativasMarcadas()
                    .stream().map(alternativa ->
                            QuestaoRealizadaDTO.builder()
                                .id(alternativa.getQuestao().getId())
                                .texto(alternativa.getQuestao().getTexto())
                                .valor(alternativa.getQuestao().getValor())
                                .alternativaMarcada(modelMapper.map(alternativa, AlternativaDTO.class))
                                .build()
                    ).toList();

            ProvaRealizadaDTO retorno = ProvaRealizadaDTO.builder()
                    .id(provaRealizada.getId())
                    .realizadaEm(provaRealizada.getRealizadaEm())
                    .nota(nota)
                    .respostasCertas(quantidadeQuestoes)
                    .questoes(questoesRespondidas)
                    .build();

            return ResponseEntity.ok(retorno);
        } catch (ProvaNaoExisteException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
