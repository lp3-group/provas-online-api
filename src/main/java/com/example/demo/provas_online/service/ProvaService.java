package com.example.demo.provas_online.service;

import com.example.demo.provas_online.exception.*;
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

    public Prova validarECriarProva(Prova prova) throws DisciplinaNaoExisteException, ProvaJaExisteException,
            QuestaoInvalidaException, AlternativaInvalidaException {
        Optional<Disciplina> disciplina = disciplinaRepository.findById(prova.getDisciplina().getId());
        Optional<Prova> provas = provaRepository.findByTitulo(prova.getTitulo());

        if(disciplina.isEmpty()) {
            throw new DisciplinaNaoExisteException();
        }

        if(provas.isPresent()) {
            throw new ProvaJaExisteException();
        }

        validarQuestoes(prova.getQuestoes());

        prova.setId(null);
        prova.setCriadaEm(new Date());
        prova.setDisciplina(disciplina.get());

        return provaRepository.save(prova);
    }

    private void validarQuestoes(List<Questao> questoes) throws QuestaoInvalidaException, AlternativaInvalidaException {
        if(questoes.isEmpty() || questoes == null) {
            throw new QuestaoInvalidaException("A prova precisa ter pelo menos uma questão!");
        }

        questoes.forEach(questao -> {
            validarAlternativas(questao.getAlternativas());
        });
    }

    private void validarAlternativas(List<Alternativa> alternativas) throws AlternativaInvalidaException {
        int QUANTIDADE_MINIMA_ALTERNATIVAS = 2;
        int QUANTIDADE_EXATA_ALTERNATIVAS_CERTAS = 1;

        if(alternativas.isEmpty() || alternativas == null || alternativas.size() < QUANTIDADE_MINIMA_ALTERNATIVAS) {
            throw new AlternativaInvalidaException("Cada questão deve ter pelo menos duas alternativas!");
        }

        int quantidadeAlternativasCertas = (int) alternativas.stream()
                .filter(alternativa -> alternativa.isRespostaCerta()).count();

        if(quantidadeAlternativasCertas != QUANTIDADE_EXATA_ALTERNATIVAS_CERTAS) {
            throw new AlternativaInvalidaException("Cada questão deve ter somente uma alternativa certa!");
        }
    }

    public List<Prova> obterProvas() {
        return provaRepository.findAll();
    }

    public void validarEExcluirProva(Integer id) throws ProvaNaoExisteException {
        Prova prova = validarEObterProvaPeloId(id);

        provaRepository.delete(prova);
    }

    public Prova validarEAtualizarProva(Prova prova)
            throws ProvaNaoExisteException, DisciplinaNaoExisteException, ProvaJaExisteException {
        Prova provaEncontrada = validar(prova);
        validarQuestoes(prova.getQuestoes());

        Disciplina disciplina =  disciplinaRepository.findById(prova.getDisciplina().getId())
                .orElseThrow(() -> new DisciplinaNaoExisteException());

        prova.setDisciplina(disciplina);
        prova.setCriadaEm(provaEncontrada.getCriadaEm());

        return provaRepository.save(prova);
    }

    private Prova validar(Prova prova)
            throws ProvaNaoExisteException, ProvaJaExisteException {
        Prova provaASerEditada = validarEObterProvaPeloId(prova.getId());
        Optional<Prova> provaEncontrada = provaRepository.findByTitulo(prova.getTitulo());

        if(provaEncontrada.isPresent() && saoProvasDiferentes(prova, provaEncontrada.get())) {
            throw new ProvaJaExisteException();
        }

        return provaASerEditada;
    }

    private boolean saoProvasDiferentes(Prova provaEditada, Prova provaEncontrada) {
        return !provaEncontrada.getId().equals(provaEditada.getId());
    }

    public Prova validarEObterProvaPeloId(Integer id) throws ProvaNaoExisteException {
        return provaRepository.findById(id).orElseThrow(() -> new ProvaNaoExisteException());
    }
}
