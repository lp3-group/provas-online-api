package com.example.demo.provas_online.service;

import com.example.demo.provas_online.exception.DisciplinaJaExisteException;
import com.example.demo.provas_online.model.entity.Disciplina;
import com.example.demo.provas_online.model.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DisciplinaService {
    @Autowired
    private DisciplinaRepository repository;

    public void validarECriarDisciplina(Disciplina disciplina) throws DisciplinaJaExisteException {
        Optional<Disciplina> disciplinaEncontrada = repository.findByNome(disciplina.getNome());

        if(disciplinaEncontrada.isPresent()) {
            throw new DisciplinaJaExisteException();
        }

        repository.save(disciplina);
    }
}