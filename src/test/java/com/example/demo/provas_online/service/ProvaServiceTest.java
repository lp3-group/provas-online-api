package com.example.demo.provas_online.service;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.provas_online.exception.*;
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
    public void testaMaisDeUmaAlternativaCerta() {
        Disciplina disciplinaTeste = new Disciplina();
        disciplinaTeste.setId(anyInt());

        Alternativa alternativaTeste1 = new Alternativa();
        alternativaTeste1.setRespostaCerta(false);
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
        replay(provaRepositoryMock);

        try {
            this.provaService.validarECriarProva(provaTeste, disciplinaRepositoryMock, provaRepositoryMock);
            fail();
        } catch (AlternativaInvalidaException e) {
            assertEquals("Cada questão deve ter somente uma alternativa certa!", e.getMessage());
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

    @Test
    public void testaObterProvas() {
        List<Prova> listaProvas = new ArrayList<>();

        ProvaRepository provaRepositoryMock = createMock(ProvaRepository.class);
        expect(provaRepositoryMock.findAll()).andReturn(listaProvas);
        replay(provaRepositoryMock);

        List<Prova> retorno = this.provaService.obterProvas(provaRepositoryMock);

        assertEquals(listaProvas, retorno);
    }

    @Test
    public void testaValidarProvaEFalhar() {
        Integer id = anyInt();

        ProvaRepository provaRepositoryMock = createMock(ProvaRepository.class);
        expect(provaRepositoryMock.findById(id)).andReturn(Optional.ofNullable(null));
        replay(provaRepositoryMock);

        try {
            this.provaService.validarEObterProvaPeloId(id, provaRepositoryMock);
            fail();
        } catch (ProvaNaoExisteException e) {
            assertEquals("Prova não encontrada!", e.getMessage());
        }
    }

    @Test
    public void testaValidarEObterProvaComSucesso() {
        Integer id = anyInt();
        Prova prova = new Prova();

        ProvaRepository provaRepositoryMock = createMock(ProvaRepository.class);
        expect(provaRepositoryMock.findById(id)).andReturn(Optional.ofNullable(prova));
        replay(provaRepositoryMock);

        Prova retorno = this.provaService.validarEObterProvaPeloId(id, provaRepositoryMock);

        assertEquals(prova, retorno);
    }

    @Test
    public void testaValidarProvaEFalharAoExcluir() {
        Integer id = anyInt();

        ProvaRepository provaRepositoryMock = createMock(ProvaRepository.class);
        expect(provaRepositoryMock.findById(id)).andReturn(Optional.ofNullable(null));
        replay(provaRepositoryMock);

        try {
            this.provaService.validarEExcluirProva(id, provaRepositoryMock);
            fail();
        } catch (ProvaNaoExisteException e) {
            assertEquals("Prova não encontrada!", e.getMessage());
        }
    }

    @Test
    public void testaValidarEExcluirProvaComSucesso() {
        Integer id = anyInt();
        Prova prova = new Prova();

        ProvaRepository provaRepositoryMock = createMock(ProvaRepository.class);
        expect(provaRepositoryMock.findById(id)).andReturn(Optional.ofNullable(prova));
        provaRepositoryMock.delete(prova);
        expectLastCall().once();
        replay(provaRepositoryMock);

        this.provaService.validarEExcluirProva(id, provaRepositoryMock);

        verify(provaRepositoryMock);
    }

    @Test
    public void testaNaoEncontrarProvaAoAtualizar() {
        Prova prova = new Prova();
        Integer id = anyInt();

        ProvaRepository provaRepositoryMock = createMock(ProvaRepository.class);
        expect(provaRepositoryMock.findById(id)).andReturn(Optional.ofNullable(null));
        replay(provaRepositoryMock);

        DisciplinaRepository disciplinaRepository = createMock(DisciplinaRepository.class);

        try {
            this.provaService.validarEAtualizarProva(prova, provaRepositoryMock, disciplinaRepository);
            fail();
        } catch (ProvaNaoExisteException e) {
            assertEquals("Prova não encontrada!", e.getMessage());
        }
    }

    @Test
    public void testaErroPorAtualizarProvasDiferentes() {
        Prova prova = new Prova();
        prova.setId(1);

        Prova provaEncontradaComMesmoTitulo = new Prova();
        provaEncontradaComMesmoTitulo.setId(2);

        ProvaRepository provaRepositoryMock = createMock(ProvaRepository.class);
        expect(provaRepositoryMock.findById(prova.getId())).andReturn(Optional.ofNullable(prova));
        expect(provaRepositoryMock.findByTitulo(prova.getTitulo())).andReturn(Optional.ofNullable(provaEncontradaComMesmoTitulo));
        replay(provaRepositoryMock);

        DisciplinaRepository disciplinaRepository = createMock(DisciplinaRepository.class);

        try {
            this.provaService.validarEAtualizarProva(prova, provaRepositoryMock, disciplinaRepository);
            fail();
        } catch (ProvaJaExisteException e) {
            assertEquals("Esta prova já existe!", e.getMessage());
        }
    }

    @Test
    public void testaErroPorNaoEncontrarADisciplina() {
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

        ProvaRepository provaRepositoryMock = createMock(ProvaRepository.class);
        expect(provaRepositoryMock.findById(provaTeste.getId())).andReturn(Optional.ofNullable(provaTeste));
        expect(provaRepositoryMock.findByTitulo(provaTeste.getTitulo())).andReturn(Optional.ofNullable(null));
        replay(provaRepositoryMock);

        DisciplinaRepository disciplinaRepositoryMock = createMock(DisciplinaRepository.class);
        expect(disciplinaRepositoryMock.findById(disciplinaTeste.getId())).andReturn(Optional.ofNullable(null));
        replay(disciplinaRepositoryMock);

        try {
            this.provaService.validarEAtualizarProva(provaTeste, provaRepositoryMock, disciplinaRepositoryMock);
            fail();
        } catch (DisciplinaNaoExisteException e) {
            assertEquals("Disciplina não encontrada!", e.getMessage());
        }
    }

    @Test
    public void testaAtualizarProvaComSucesso() {
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

        ProvaRepository provaRepositoryMock = createMock(ProvaRepository.class);
        expect(provaRepositoryMock.findById(provaTeste.getId())).andReturn(Optional.ofNullable(provaTeste));
        expect(provaRepositoryMock.findByTitulo(provaTeste.getTitulo())).andReturn(Optional.ofNullable(null));
        expect(provaRepositoryMock.save(provaTeste)).andReturn(provaTeste);
        replay(provaRepositoryMock);

        DisciplinaRepository disciplinaRepositoryMock = createMock(DisciplinaRepository.class);
        expect(disciplinaRepositoryMock.findById(disciplinaTeste.getId())).andReturn(Optional.ofNullable(disciplinaTeste));
        replay(disciplinaRepositoryMock);

        Prova retorno = this.provaService.validarEAtualizarProva(provaTeste, provaRepositoryMock, disciplinaRepositoryMock);

        assertEquals(provaTeste, retorno);
    }
}
