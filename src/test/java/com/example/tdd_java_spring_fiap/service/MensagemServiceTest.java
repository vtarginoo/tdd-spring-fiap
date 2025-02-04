package com.example.tdd_java_spring_fiap.service;


import com.example.tdd_java_spring_fiap.exception.MensagemNotFoundException;
import com.example.tdd_java_spring_fiap.model.Mensagem;
import com.example.tdd_java_spring_fiap.repository.MensagemRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MensagemServiceTest {

    private MensagemService mensagemService;

    @Mock
    private MensagemRepository mensagemRepository;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        mensagemService = new MensagemServiceImpl(mensagemRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }


    @Test
    void devePermitirRegistrarMensagem() {
        //Arrange
        var mensagem = gerarMensagem();
        when(mensagemRepository.save(any(Mensagem.class))).thenReturn(mensagem);

        //Act
        var mensagemRegistrada = mensagemService.registrarMensagem(mensagem);

        //Assert
        assertThat(mensagemRegistrada).isInstanceOf(Mensagem.class).isNotNull();

        assertThat(mensagemRegistrada.getConteudo()).isEqualTo(mensagem.getConteudo());

        assertThat(mensagemRegistrada.getUsuario()).isEqualTo(mensagem.getUsuario());

        verify(mensagemRepository, times(1)).save(mensagem);
    }

    @Test
    void devePermitirBuscarMensagem() {
        // Arrange
        var id = UUID.randomUUID();
        var mensagem = gerarMensagem();
        mensagem.setId(id);

        when(mensagemRepository.findById(any(UUID.class))).thenReturn(Optional.of(mensagem));
        // Act
        var mensagemObtida = mensagemService.buscarMensagem(id);

        // Assert
        assertThat(mensagemObtida).isEqualTo(mensagem);
        verify(mensagemRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExistente() {
        var id = UUID.randomUUID();

        when(mensagemRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mensagemService.buscarMensagem(id)).isInstanceOf(MensagemNotFoundException.class).hasMessage("mensagem não encontrada");
        verify(mensagemRepository, times(1)).findById(id);
    }


    @Nested
    class AlterarMensagem {

        @Test
        void devePermirirAlterarMensagem() {
            var id = UUID.randomUUID();
            var mensagemAntiga = gerarMensagem();
            mensagemAntiga.setId(id);
            var mensagemNova = mensagemAntiga;
            mensagemNova.setConteudo("abcd");

            when(mensagemRepository.findById(any(UUID.class))).thenReturn(Optional.of(mensagemAntiga));

            when(mensagemRepository.save(any(Mensagem.class))).thenAnswer(i -> i.getArgument(0));

            var mensagemObtida = mensagemService.alterarMensagem(id, mensagemNova);

            assertThat(mensagemObtida).isInstanceOf(Mensagem.class).isNotNull();
            assertThat(mensagemObtida.getId()).isEqualTo(mensagemNova.getId());
            assertThat(mensagemObtida.getUsuario()).isEqualTo(mensagemNova.getUsuario());
            assertThat(mensagemObtida.getConteudo()).isEqualTo(mensagemNova.getConteudo());
            verify(mensagemRepository, times(1)).save(any(Mensagem.class));
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_IdNaoCoincide() {
            var id = UUID.randomUUID();
            var mensagemAntiga = gerarMensagem();
            mensagemAntiga.setId(id);
            var mensagemNova = Mensagem.builder().build();
            mensagemNova.setId(UUID.randomUUID());

            assertThatThrownBy(() -> mensagemService.alterarMensagem(id, mensagemNova)).isInstanceOf(MensagemNotFoundException.class).hasMessage("mensagem não encontrada");
            verify(mensagemRepository, never()).save(any(Mensagem.class));
        }

    }


    private Mensagem gerarMensagem() {
        return Mensagem.builder().usuario("José").conteudo("conteúdo da mensagem").build();
    }


}
