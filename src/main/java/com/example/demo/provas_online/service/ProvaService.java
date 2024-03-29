package com.example.demo.provas_online.service;

import com.example.demo.provas_online.exception.*;
import com.example.demo.provas_online.model.entity.*;
import com.example.demo.provas_online.model.repository.AlternativaRepository;
import com.example.demo.provas_online.model.repository.DisciplinaRepository;
import com.example.demo.provas_online.model.repository.ProvaRealizadaRepository;
import com.example.demo.provas_online.model.repository.ProvaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProvaService {
    @Autowired
    private ProvaRepository provaRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private AlternativaRepository alternativaRepository;

    @Autowired
    private ProvaRealizadaRepository provaRealizadaRepository;

    public Prova validarECriarProva(Prova prova, DisciplinaRepository disciplinaRepository, ProvaRepository provaRepository) throws DisciplinaNaoExisteException, ProvaJaExisteException,
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
        if(questoes == null || questoes.isEmpty()) {
            throw new QuestaoInvalidaException("A prova precisa ter pelo menos uma questão!");
        }

        questoes.forEach(questao -> {
            validarAlternativas(questao.getAlternativas());
        });
    }

    private void validarAlternativas(List<Alternativa> alternativas) throws AlternativaInvalidaException {
        int QUANTIDADE_MINIMA_ALTERNATIVAS = 2;
        int QUANTIDADE_EXATA_ALTERNATIVAS_CERTAS = 1;

        if(alternativas == null || alternativas.isEmpty() || alternativas.size() < QUANTIDADE_MINIMA_ALTERNATIVAS) {
            throw new AlternativaInvalidaException("Cada questão deve ter pelo menos duas alternativas!");
        }

        int quantidadeAlternativasCertas = (int) alternativas.stream()
                .filter(alternativa -> alternativa.isRespostaCerta()).count();

        if(quantidadeAlternativasCertas != QUANTIDADE_EXATA_ALTERNATIVAS_CERTAS) {
            throw new AlternativaInvalidaException("Cada questão deve ter somente uma alternativa certa!");
        }
    }

    public List<Prova> obterProvas(ProvaRepository provaRepository) {
        return provaRepository.findAll();
    }

    public void validarEExcluirProva(Integer id, ProvaRepository provaRepository) throws ProvaNaoExisteException {
        Prova prova = validarEObterProvaPeloId(id, provaRepository);

        provaRepository.delete(prova);
    }

    public Prova validarEAtualizarProva(Prova prova, ProvaRepository provaRepository, DisciplinaRepository disciplinaRepository)
            throws ProvaNaoExisteException, DisciplinaNaoExisteException, ProvaJaExisteException {
        Prova provaEncontrada = validar(prova, provaRepository);
        validarQuestoes(prova.getQuestoes());

        Disciplina disciplina =  disciplinaRepository.findById(prova.getDisciplina().getId())
                .orElseThrow(() -> new DisciplinaNaoExisteException());

        prova.setDisciplina(disciplina);
        prova.setCriadaEm(provaEncontrada.getCriadaEm());

        return provaRepository.save(prova);
    }

    private Prova validar(Prova prova, ProvaRepository provaRepository)
            throws ProvaNaoExisteException, ProvaJaExisteException {
        Prova provaASerEditada = validarEObterProvaPeloId(prova.getId(), provaRepository);
        Optional<Prova> provaEncontrada = provaRepository.findByTitulo(prova.getTitulo());

        if(provaEncontrada.isPresent() && saoProvasDiferentes(prova, provaEncontrada.get())) {
            throw new ProvaJaExisteException();
        }

        return provaASerEditada;
    }

    private boolean saoProvasDiferentes(Prova provaEditada, Prova provaEncontrada) {
        return !provaEncontrada.getId().equals(provaEditada.getId());
    }

    public Prova validarEObterProvaRandomizada(Integer id) throws ProvaNaoExisteException {
        Prova prova = validarEObterProvaPeloId(id, provaRepository);

        prova.getQuestoes().forEach(questao -> {
            Collections.shuffle(questao.getAlternativas());
        });

        Collections.shuffle(prova.getQuestoes());

        return prova;
    }

    public Prova validarEObterProvaPeloId(Integer id, ProvaRepository provaRepository) throws ProvaNaoExisteException {
        return provaRepository.findById(id).orElseThrow(() -> new ProvaNaoExisteException());
    }

    public ProvaRealizada realizarProva(Estudante estudante, Prova prova, List<Integer> idsAlternativas) {
        List<Alternativa> alternativas = getAlternativasPeloId(idsAlternativas);

        ProvaRealizada provaRealizada = ProvaRealizada.builder()
                .realizadaEm(new Date())
                .estudante(estudante)
                .prova(prova)
                .alternativasMarcadas(alternativas)
                .build();

        return provaRealizadaRepository.save(provaRealizada);
    }

    @Transactional
    private List<Alternativa> getAlternativasPeloId(List<Integer> idsAlternativas) {
        return alternativaRepository.findAllById(idsAlternativas);
    }

    public double calcularNota(List<Alternativa> alternativasMarcadas) {
        return filtrarQuestoesCertas(alternativasMarcadas).stream().mapToDouble(
                alternativa -> alternativa.getQuestao().getValor()
        ).sum();
    }

    public List<Alternativa> filtrarQuestoesCertas(List<Alternativa> alternativasMarcadas) {
        return alternativasMarcadas.stream().filter(Alternativa::isRespostaCerta).toList();
    }
}
