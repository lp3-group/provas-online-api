package com.example.demo.provas_online.model.repository;

import com.example.demo.provas_online.model.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
    Optional<Administrador> findByNomeUsuario(String nomeUsuario);
}
