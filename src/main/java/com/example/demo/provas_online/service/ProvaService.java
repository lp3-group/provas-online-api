package com.example.demo.provas_online.service;

import com.example.demo.provas_online.exception.DisciplinaNaoExisteException;
import com.example.demo.provas_online.exception.ProvaJaExisteException;
import com.example.demo.provas_online.model.entity.Disciplina;
import com.example.demo.provas_online.model.entity.Prova;
import com.example.demo.provas_online.model.repository.DisciplinaRepository;
import com.example.demo.provas_online.model.repository.ProvaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ProvaService {
    @Autowired
    private ProvaRepository provaRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    public Prova validarECriarProva(Prova prova) throws DisciplinaNaoExisteException, ProvaJaExisteException {
        Optional<Disciplina> disciplina = disciplinaRepository.findById(prova.getDisciplina().getId());
        Optional<Prova> provas = provaRepository.findByTitulo(prova.getTitulo());

        if(disciplina.isEmpty()) {
            throw new DisciplinaNaoExisteException();
        }

        if(provas.isPresent()) {
            throw new ProvaJaExisteException();
        }

        prova.setId(null);
        prova.setCriadaEm(new Date());
        prova.setDisciplina(disciplina.get());

        return provaRepository.save(prova);
    }
}
