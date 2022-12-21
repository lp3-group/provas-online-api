package com.example.demo.provas_online.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@Entity
@SuperBuilder
public class Administrador extends Usuario {
}
