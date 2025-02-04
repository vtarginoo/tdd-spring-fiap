package com.example.tdd_java_spring_fiap.exception;

public class MensagemNotFoundException extends RuntimeException  {
    public MensagemNotFoundException(String mensagem) {
        super(mensagem);
    }
}
