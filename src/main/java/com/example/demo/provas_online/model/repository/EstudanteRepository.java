package com.example.demo.provas_online.model.repository;

import com.example.demo.provas_online.model.entity.Administrador;
import com.example.demo.provas_online.model.entity.Estudante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstudanteRepository extends JpaRepository<Estudante, Integer> {
    Optional<Estudante> findByNomeUsuario(String nomeUsuario);
}
