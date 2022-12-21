package com.example.demo.provas_online.model.repository;

import com.example.demo.provas_online.model.entity.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Integer> {
    Optional<Disciplina> findByNome(String nome);
}
