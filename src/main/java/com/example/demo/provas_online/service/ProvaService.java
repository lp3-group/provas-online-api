package com.example.demo.provas_online.service;

import com.example.demo.provas_online.exception.DisciplinaNaoExisteException;
import com.example.demo.provas_online.exception.ProvaJaExisteException;
import com.example.demo.provas_online.exception.ProvaNaoExisteException;
import com.example.demo.provas_online.model.entity.Alternativa;
import com.example.demo.provas_online.model.entity.Disciplina;
import com.example.demo.provas_online.model.entity.Prova;
import com.example.demo.provas_online.model.entity.Questao;
import com.example.demo.provas_online.model.repository.DisciplinaRepository;
import com.example.demo.provas_online.model.repository.ProvaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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

    public List<Prova> obterProvas() {
        return provaRepository.findAll();
    }

    public void validarEExcluirProva(Integer id) throws ProvaNaoExisteException {
        Prova prova = validarEObterProvaPeloId(id);

        provaRepository.delete(prova);
    }

    public Prova validarEObterProvaPeloId(Integer id) throws ProvaNaoExisteException {
        Optional<Prova> prova = obterProvaPeloId(id);

        if(prova.isEmpty()) {
            throw new ProvaNaoExisteException();
        }

        return prova.get();
    }

    public Optional<Prova> obterProvaPeloId(Integer id) {
        return provaRepository.findById(id);
    }
}
