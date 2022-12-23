package com.example.demo.provas_online.exception;

public class SenhaInvalidaException extends RuntimeException {
    public SenhaInvalidaException() {
        super("Senha inválida");
    }

    public SenhaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
