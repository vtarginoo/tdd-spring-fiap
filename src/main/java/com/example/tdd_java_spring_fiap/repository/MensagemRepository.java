package com.example.tdd_java_spring_fiap.repository;

import com.example.tdd_java_spring_fiap.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MensagemRepository extends JpaRepository<Mensagem, UUID> {


}
