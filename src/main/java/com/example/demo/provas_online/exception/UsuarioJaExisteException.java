package com.example.demo.provas_online.exception;

public class UsuarioJaExisteException extends RuntimeException {
    public UsuarioJaExisteException() {
        super("Este usuário já existe");
    }
}
