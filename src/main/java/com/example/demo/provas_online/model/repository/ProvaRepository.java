package com.example.demo.provas_online.model.repository;

import com.example.demo.provas_online.model.entity.Prova;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProvaRepository extends JpaRepository<Prova, Integer> {
    Optional<Prova> findByTitulo(String titulo);
}
