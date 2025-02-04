package com.example.tdd_java_spring_fiap.service;

import com.example.tdd_java_spring_fiap.model.Mensagem;

import java.util.Optional;
import java.util.UUID;

public interface MensagemService {

    Mensagem registrarMensagem(Mensagem mensagem);

    Mensagem buscarMensagem(UUID id);

    Mensagem alterarMensagem(UUID id, Mensagem mensagemAtualizada);

    boolean removerMensagem(UUID id);


}
