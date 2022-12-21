package com.example.demo.provas_online.exception;

public class DisciplinaJaExisteException extends RuntimeException {
    public DisciplinaJaExisteException() {
        super("Esta disciplina já existe!");
    }
}
