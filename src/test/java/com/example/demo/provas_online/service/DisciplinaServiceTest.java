package com.example.demo.provas_online.service;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.provas_online.exception.DisciplinaJaExisteException;
import com.example.demo.provas_online.model.entity.Disciplina;
import com.example.demo.provas_online.model.repository.DisciplinaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DisciplinaServiceTest {
    private DisciplinaService disciplinaService;

    @BeforeAll
    void init() {
        this.disciplinaService = new DisciplinaService();
    }

    @Test
    public void testaDisciplinaEncontrada() {
        DisciplinaRepository repositoryMock = createMock(DisciplinaRepository.class);
        expect(repositoryMock.findByNome(new String())).andReturn(Optional.ofNullable(new Disciplina()));
        replay(repositoryMock);

        try {
            this.disciplinaService.validarECriarDisciplina(new Disciplina(), repositoryMock);
            fail();
        } catch (DisciplinaJaExisteException e) {
            assertEquals("Esta disciplina já existe!", e.getMessage());
        }
    }

    @Test
    public void testaDisciplinaCriada() {
        DisciplinaRepository repositoryMock = createMock(DisciplinaRepository.class);
        expect(repositoryMock.findByNome(anyObject())).andReturn(Optional.ofNullable(null));
        expect(repositoryMock.save(anyObject())).andReturn(Disciplina.class);
        replay(repositoryMock);

        this.disciplinaService.validarECriarDisciplina(new Disciplina(), repositoryMock);

        verify(repositoryMock);
    }

    @Test
    public void testaObterDisciplinas() {
        DisciplinaRepository repositoryMock = createMock(DisciplinaRepository.class);
        expect(repositoryMock.findAll()).andReturn(new ArrayList<Disciplina>());
        replay(repositoryMock);

        List<Disciplina> retorno = this.disciplinaService.obterDisciplinas(repositoryMock);

        assertInstanceOf(List.class, retorno);
    }
}
