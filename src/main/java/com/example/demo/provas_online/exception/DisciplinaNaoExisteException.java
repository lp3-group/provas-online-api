package com.example.demo.provas_online.exception;

public class DisciplinaNaoExisteException extends RuntimeException{
    public DisciplinaNaoExisteException() {
        super("Disciplina não encontrada!");
    }
}
