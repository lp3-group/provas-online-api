package com.example.demo.provas_online.service;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.provas_online.exception.DisciplinaJaExisteException;
import com.example.demo.provas_online.model.entity.Disciplina;
import com.example.demo.provas_online.model.repository.DisciplinaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

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
}
