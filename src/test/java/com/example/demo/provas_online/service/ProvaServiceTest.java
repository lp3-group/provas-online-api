package com.example.demo.provas_online.service;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.provas_online.exception.AlternativaInvalidaException;
import com.example.demo.provas_online.exception.DisciplinaNaoExisteException;
import com.example.demo.provas_online.exception.ProvaJaExisteException;
import com.example.demo.provas_online.exception.QuestaoInvalidaException;
import com.example.demo.provas_online.model.entity.Alternativa;
import com.example.demo.provas_online.model.entity.Disciplina;
import com.example.demo.provas_online.model.entity.Prova;
import com.example.demo.provas_online.model.entity.Questao;
import com.example.demo.provas_online.model.repository.DisciplinaRepository;
import com.example.demo.provas_online.model.repository.ProvaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProvaServiceTest {
    private ProvaService provaService;

    @BeforeAll
    void init() {
        this.provaService = new ProvaService();
    }

    @Test
    public void testaDisciplinaNaoEncontradaAoCriarProva() {
        Disciplina disciplinaTeste = new Disciplina();
        disciplinaTeste.setId(anyInt());

        Prova provaTeste = new Prova();
        provaTeste.setDisciplina(disciplinaTeste);

        DisciplinaRepository disciplinaRepositoryMock = createMock(DisciplinaRepository.class);
        expect(disciplinaRepositoryMock.findById(provaTeste.getDisciplina().getId())).andReturn(Optional.ofNullable(null));
        replay(disciplinaRepositoryMock);

        ProvaRepository provaRepositoryMock = createMock(ProvaRepository.class);

        try {
            this.provaService.validarECriarProva(provaTeste, disciplinaRepositoryMock, provaRepositoryMock);
            fail();
        } catch (DisciplinaNaoExisteException e) {
            assertEquals("Disciplina não encontrada!", e.getMessage());
        }
    }

    @Test
    public void testaProvaEncontradaAoTentarCriar() {
        Disciplina disciplinaTeste = new Disciplina();
        disciplinaTeste.setId(anyInt());

        Prova provaTeste = new Prova();
        provaTeste.setDisciplina(disciplinaTeste);

        DisciplinaRepository disciplinaRepositoryMock = createMock(DisciplinaRepository.class);
        expect(disciplinaRepositoryMock.findById(provaTeste.getDisciplina().getId())).andReturn(Optional.ofNullable(disciplinaTeste));
        replay(disciplinaRepositoryMock);

        ProvaRepository provaRepositoryMock = createMock(ProvaRepository.class);
        expect(provaRepositoryMock.findByTitulo(provaTeste.getTitulo())).andReturn(Optional.ofNullable(provaTeste));
        replay(provaRepositoryMock);

        try {
            this.provaService.validarECriarProva(provaTeste, disciplinaRepositoryMock, provaRepositoryMock);
            fail();
        } catch (ProvaJaExisteException e) {
            assertEquals("Esta prova já existe!", e.getMessage());
        }
    }

    @Test
    public void testaQuestaoInvalidaAoValidar() {
        Disciplina disciplinaTeste = new Disciplina();
        disciplinaTeste.setId(anyInt());

        Prova provaTeste = new Prova();
        provaTeste.setDisciplina(disciplinaTeste);

        DisciplinaRepository disciplinaRepositoryMock = createMock(DisciplinaRepository.class);
        expect(disciplinaRepositoryMock.findById(provaTeste.getDisciplina().getId())).andReturn(Optional.ofNullable(disciplinaTeste));
        replay(disciplinaRepositoryMock);

        ProvaRepository provaRepositoryMock = createMock(ProvaRepository.class);
        expect(provaRepositoryMock.findByTitulo(provaTeste.getTitulo())).andReturn(Optional.ofNullable(null));
        replay(provaRepositoryMock);

        try {
            this.provaService.validarECriarProva(provaTeste, disciplinaRepositoryMock, provaRepositoryMock);
            fail();
        } catch (QuestaoInvalidaException e) {
            assertEquals("A prova precisa ter pelo menos uma questão!", e.getMessage());
        }
    }

    @Test
    public void testaMenosDeDuasAlternativas() {
        Disciplina disciplinaTeste = new Disciplina();
        disciplinaTeste.setId(anyInt());

        Questao questaoTeste = new Questao();
        List<Questao> questoesTeste = new ArrayList<Questao>();
        questoesTeste.add(questaoTeste);

        Prova provaTeste = new Prova();
        provaTeste.setDisciplina(disciplinaTeste);
        provaTeste.setQuestoes(questoesTeste);

        DisciplinaRepository disciplinaRepositoryMock = createMock(DisciplinaRepository.class);
        expect(disciplinaRepositoryMock.findById(provaTeste.getDisciplina().getId())).andReturn(Optional.ofNullable(disciplinaTeste));
        replay(disciplinaRepositoryMock);

        ProvaRepository provaRepositoryMock = createMock(ProvaRepository.class);
        expect(provaRepositoryMock.findByTitulo(provaTeste.getTitulo())).andReturn(Optional.ofNullable(null));
        replay(provaRepositoryMock);

        try {
            this.provaService.validarECriarProva(provaTeste, disciplinaRepositoryMock, provaRepositoryMock);
            fail();
        } catch (AlternativaInvalidaException e) {
            assertEquals("Cada questão deve ter pelo menos duas alternativas!", e.getMessage());
        }
    }

    @Test
    public void testaCriarProvaComSucesso() {
        Disciplina disciplinaTeste = new Disciplina();
        disciplinaTeste.setId(anyInt());

        Alternativa alternativaTeste1 = new Alternativa();
        alternativaTeste1.setRespostaCerta(true);
        Alternativa alternativaTeste2 = new Alternativa();
        alternativaTeste2.setRespostaCerta(false);

        List<Alternativa> alternativasTeste = new ArrayList<Alternativa>();
        alternativasTeste.add(alternativaTeste1);
        alternativasTeste.add(alternativaTeste2);

        Questao questaoTeste = new Questao();
        questaoTeste.setAlternativas(alternativasTeste);
        List<Questao> questoesTeste = new ArrayList<Questao>();
        questoesTeste.add(questaoTeste);

        Prova provaTeste = new Prova();
        provaTeste.setDisciplina(disciplinaTeste);
        provaTeste.setQuestoes(questoesTeste);

        DisciplinaRepository disciplinaRepositoryMock = createMock(DisciplinaRepository.class);
        expect(disciplinaRepositoryMock.findById(provaTeste.getDisciplina().getId())).andReturn(Optional.ofNullable(disciplinaTeste));
        replay(disciplinaRepositoryMock);

        ProvaRepository provaRepositoryMock = createMock(ProvaRepository.class);
        expect(provaRepositoryMock.findByTitulo(provaTeste.getTitulo())).andReturn(Optional.ofNullable(null));
        expect(provaRepositoryMock.save(provaTeste)).andReturn(provaTeste);
        replay(provaRepositoryMock);

        Prova retorno = this.provaService.validarECriarProva(provaTeste, disciplinaRepositoryMock, provaRepositoryMock);

        assertEquals(provaTeste, retorno);
    }
}
