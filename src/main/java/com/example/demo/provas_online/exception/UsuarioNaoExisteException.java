package com.example.demo.provas_online.exception;

public class UsuarioNaoExisteException extends RuntimeException {
    public UsuarioNaoExisteException() {
        super("Este não usuário existe");
    }
}
