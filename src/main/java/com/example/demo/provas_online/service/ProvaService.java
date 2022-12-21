package com.example.demo.provas_online.service;

import com.example.demo.provas_online.model.entity.Prova;
import com.example.demo.provas_online.model.repository.ProvaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProvaService {
    @Autowired
    private ProvaRepository repository;

    public Prova criarProva(Prova prova) {
        prova.setCriadaEm(new Date());

        return repository.save(prova);
    }
}
