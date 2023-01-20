package com.example.demo.provas_online.service;

import com.example.demo.provas_online.exception.DisciplinaJaExisteException;
import com.example.demo.provas_online.exception.DisciplinaNaoExisteException;
import com.example.demo.provas_online.model.entity.Disciplina;
import com.example.demo.provas_online.model.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisciplinaService {
    @Autowired
    private DisciplinaRepository repository;

    public void validarECriarDisciplina(Disciplina disciplina, DisciplinaRepository repository) throws DisciplinaJaExisteException {
        Optional<Disciplina> disciplinaEncontrada = repository.findByNome(disciplina.getNome());

        if(disciplinaEncontrada.isPresent()) {
            throw new DisciplinaJaExisteException();
        }

        repository.save(disciplina);
    }

    public List<Disciplina> obterDisciplinas(DisciplinaRepository repository) {
        return repository.findAll();
    }

    public Disciplina validarEEditarDisciplina(Disciplina disciplina) throws DisciplinaNaoExisteException {
        Optional<Disciplina> disciplinaEncontrada = repository.findById(disciplina.getId());

        if(disciplinaEncontrada.isEmpty()) {
            throw new DisciplinaNaoExisteException();
        }

        return repository.save(disciplina);
    }

    public void validarEExcluirDisciplina(Integer id) throws DisciplinaNaoExisteException {
        Optional<Disciplina> disciplinaEncontrada = repository.findById(id);

        if(disciplinaEncontrada.isEmpty()) {
            throw new DisciplinaNaoExisteException();
        }

        repository.delete(disciplinaEncontrada.get());
    }
}
