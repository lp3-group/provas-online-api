package com.example.demo.provas_online.exception;

public class ProvaJaExisteException extends RuntimeException{
    public ProvaJaExisteException() {
        super("Esta prova já existe!");
    }
}
