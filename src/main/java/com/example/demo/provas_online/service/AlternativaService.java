package com.example.demo.provas_online.service;

import com.example.demo.provas_online.model.entity.Alternativa;
import com.example.demo.provas_online.model.repository.AlternativaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlternativaService {
    @Autowired
    private AlternativaRepository repository;

    public List<Alternativa> getAlternativasPorIds(List<Integer> ids) {
        return repository.findAllById(ids);
    }
}
