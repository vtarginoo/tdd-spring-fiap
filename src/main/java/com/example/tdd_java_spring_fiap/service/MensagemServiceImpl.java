package com.example.tdd_java_spring_fiap.service;

import com.example.tdd_java_spring_fiap.exception.MensagemNotFoundException;
import com.example.tdd_java_spring_fiap.model.Mensagem;
import com.example.tdd_java_spring_fiap.repository.MensagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MensagemServiceImpl implements MensagemService {


    private final MensagemRepository mensagemRepository;


    @Override
    public Mensagem registrarMensagem(Mensagem mensagem) {
        mensagem.setId(UUID.randomUUID());
        return mensagemRepository.save(mensagem);
    }

    @Override
    public Mensagem buscarMensagem(UUID id) {
        return mensagemRepository
                .findById(id).orElseThrow(() -> new MensagemNotFoundException("Mensagem Não encontrada"));
    }

    @Override
    public Mensagem alterarMensagem(UUID id, Mensagem mensagemAtualizada) {
        var mensagem = buscarMensagem(id);

        if(!mensagem.getId().equals(mensagemAtualizada.getId())) {
            throw new  MensagemNotFoundException("Mensagem atualizada não apresenta o id correto");
        }

        mensagem.setConteudo(mensagemAtualizada.getConteudo());

        return mensagemRepository.save(mensagem);
    }

    @Override
    public boolean removerMensagem(UUID id) {
        mensagemRepository.deleteById(id);
        return true;
    }
}
