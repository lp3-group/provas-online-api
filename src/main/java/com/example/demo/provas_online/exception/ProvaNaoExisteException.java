package com.example.demo.provas_online.exception;

public class ProvaNaoExisteException extends RuntimeException{
    public ProvaNaoExisteException() {
        super("Prova não encontrada!");
    }
}
