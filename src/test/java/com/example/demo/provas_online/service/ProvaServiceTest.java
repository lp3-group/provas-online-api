package com.example.demo.provas_online.service;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.provas_online.exception.DisciplinaNaoExisteException;
import com.example.demo.provas_online.model.entity.Disciplina;
import com.example.demo.provas_online.model.entity.Prova;
import com.example.demo.provas_online.model.repository.DisciplinaRepository;
import com.example.demo.provas_online.model.repository.ProvaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

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
        expect(disciplinaRepositoryMock.findById(provaTeste.getId())).andReturn(Optional.ofNullable(null));
        replay(disciplinaRepositoryMock);

        ProvaRepository provaRepositoryMock = createMock(ProvaRepository.class);

        try {
            this.provaService.validarECriarProva(provaTeste, disciplinaRepositoryMock, provaRepositoryMock);
            fail();
        } catch (DisciplinaNaoExisteException e) {
            assertEquals("Disciplina não encontrada!", e.getMessage());
        }
    }
}
