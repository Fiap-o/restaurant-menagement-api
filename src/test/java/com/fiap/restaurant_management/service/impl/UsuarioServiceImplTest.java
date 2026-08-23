package com.fiap.restaurant_management.service.impl;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fiap.restaurant_management.dto.EnderecoDTO;
import com.fiap.restaurant_management.dto.SenhaUpdateDTO;
import com.fiap.restaurant_management.dto.UsuarioCreateDTO;
import com.fiap.restaurant_management.dto.UsuarioResponseDTO;
import com.fiap.restaurant_management.dto.UsuarioUpdateDTO;
import com.fiap.restaurant_management.entities.Endereco;
import com.fiap.restaurant_management.entities.TipoUsuario;
import com.fiap.restaurant_management.entities.Usuario;
import com.fiap.restaurant_management.exception.EmailJaCadastradoException;
import com.fiap.restaurant_management.exception.LoginJaCadastradoException;
import com.fiap.restaurant_management.exception.SenhaAtualInvalidaException;
import com.fiap.restaurant_management.exception.UsuarioNaoEncontradoException;
import com.fiap.restaurant_management.repositories.UsuarioRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioServiceImpl(usuarioRepository, passwordEncoder);
    }

    private Usuario usuarioExistente() {
        return Usuario.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao.silva@email.com")
                .login("joaosilva")
                .senha("hash-antigo")
                .tipoUsuario(TipoUsuario.CLIENTE)
                .endereco(new Endereco("Rua das Flores", "123", "São Paulo", "01001-000"))
                .build();
    }

    private UsuarioCreateDTO createDTOValido() {
        return new UsuarioCreateDTO(
                "João Silva",
                "joao.silva@email.com",
                "joaosilva",
                "senha123",
                TipoUsuario.CLIENTE,
                new EnderecoDTO("Rua das Flores", "123", "São Paulo", "01001-000"));
    }

    @Test
    void criar_deveCriarUsuarioComSucesso_quandoEmailELoginDisponiveis() {
        UsuarioCreateDTO dto = createDTOValido();
        given(usuarioRepository.existsByEmail(dto.getEmail())).willReturn(false);
        given(usuarioRepository.findByLogin(dto.getLogin())).willReturn(Optional.empty());
        given(passwordEncoder.encode(dto.getSenha())).willReturn("senha-hash");
        given(usuarioRepository.save(any(Usuario.class))).willAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(1L);
            return usuario;
        });

        UsuarioResponseDTO resultado = usuarioService.criar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getEmail()).isEqualTo(dto.getEmail());
        assertThat(resultado.getDataUltimaAlteracao()).isNotNull();

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getSenha()).isEqualTo("senha-hash");
    }

    @Test
    void criar_deveLancarExcecao_quandoEmailJaCadastrado() {
        UsuarioCreateDTO dto = createDTOValido();
        given(usuarioRepository.existsByEmail(dto.getEmail())).willReturn(true);

        assertThatThrownBy(() -> usuarioService.criar(dto))
                .isInstanceOf(EmailJaCadastradoException.class)
                .hasMessageContaining(dto.getEmail());

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void criar_deveLancarExcecao_quandoLoginJaCadastrado() {
        UsuarioCreateDTO dto = createDTOValido();
        given(usuarioRepository.existsByEmail(dto.getEmail())).willReturn(false);
        given(usuarioRepository.findByLogin(dto.getLogin())).willReturn(Optional.of(usuarioExistente()));

        assertThatThrownBy(() -> usuarioService.criar(dto))
                .isInstanceOf(LoginJaCadastradoException.class)
                .hasMessageContaining(dto.getLogin());

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void atualizarDados_deveAtualizarComSucesso() {
        Usuario existente = usuarioExistente();
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(
                "João Silva Santos",
                existente.getEmail(),
                existente.getLogin(),
                new EnderecoDTO("Rua Nova", "456", "São Paulo", "01002-000"));

        given(usuarioRepository.findById(1L)).willReturn(Optional.of(existente));
        given(usuarioRepository.save(any(Usuario.class))).willAnswer(invocation -> invocation.getArgument(0));

        UsuarioResponseDTO resultado = usuarioService.atualizarDados(1L, dto);

        assertThat(resultado.getNome()).isEqualTo("João Silva Santos");
        assertThat(resultado.getEndereco().getRua()).isEqualTo("Rua Nova");
    }

    @Test
    void atualizarDados_deveLancarExcecao_quandoUsuarioNaoEncontrado() {
        given(usuarioRepository.findById(99L)).willReturn(Optional.empty());
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO("Nome", "email@email.com", "login",
                new EnderecoDTO("Rua", "1", "Cidade", "00000-000"));

        assertThatThrownBy(() -> usuarioService.atualizarDados(99L, dto))
                .isInstanceOf(UsuarioNaoEncontradoException.class);

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void atualizarDados_deveLancarExcecao_quandoNovoEmailJaUsadoPorOutroUsuario() {
        Usuario existente = usuarioExistente();
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(
                existente.getNome(), "outro.email@email.com", existente.getLogin(),
                new EnderecoDTO("Rua", "1", "Cidade", "00000-000"));

        given(usuarioRepository.findById(1L)).willReturn(Optional.of(existente));
        given(usuarioRepository.existsByEmail("outro.email@email.com")).willReturn(true);

        assertThatThrownBy(() -> usuarioService.atualizarDados(1L, dto))
                .isInstanceOf(EmailJaCadastradoException.class);

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void atualizarSenha_deveAtualizarComSucesso_quandoSenhaAtualCorreta() {
        Usuario existente = usuarioExistente();
        SenhaUpdateDTO dto = new SenhaUpdateDTO("senhaAtual", "novaSenha");

        given(usuarioRepository.findById(1L)).willReturn(Optional.of(existente));
        given(passwordEncoder.matches("senhaAtual", existente.getSenha())).willReturn(true);
        given(passwordEncoder.encode("novaSenha")).willReturn("nova-senha-hash");

        usuarioService.atualizarSenha(1L, dto);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getSenha()).isEqualTo("nova-senha-hash");
    }

    @Test
    void atualizarSenha_deveLancarExcecao_quandoSenhaAtualIncorreta() {
        Usuario existente = usuarioExistente();
        SenhaUpdateDTO dto = new SenhaUpdateDTO("senhaErrada", "novaSenha");

        given(usuarioRepository.findById(1L)).willReturn(Optional.of(existente));
        given(passwordEncoder.matches("senhaErrada", existente.getSenha())).willReturn(false);

        assertThatThrownBy(() -> usuarioService.atualizarSenha(1L, dto))
                .isInstanceOf(SenhaAtualInvalidaException.class);

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void excluir_deveExcluirComSucesso() {
        Usuario existente = usuarioExistente();
        given(usuarioRepository.findById(1L)).willReturn(Optional.of(existente));

        usuarioService.excluir(1L);

        verify(usuarioRepository, times(1)).delete(existente);
    }

    @Test
    void excluir_deveLancarExcecao_quandoUsuarioNaoEncontrado() {
        given(usuarioRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.excluir(99L))
                .isInstanceOf(UsuarioNaoEncontradoException.class);

        verify(usuarioRepository, never()).delete(any());
    }

    @Test
    void buscarPorId_deveRetornarUsuario_quandoEncontrado() {
        given(usuarioRepository.findById(1L)).willReturn(Optional.of(usuarioExistente()));

        UsuarioResponseDTO resultado = usuarioService.buscarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getLogin()).isEqualTo("joaosilva");
    }

    @Test
    void buscarPorId_deveLancarExcecao_quandoNaoEncontrado() {
        given(usuarioRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.buscarPorId(99L))
                .isInstanceOf(UsuarioNaoEncontradoException.class);
    }

    @Test
    void buscarPorNome_deveRetornarListaDeUsuarios() {
        given(usuarioRepository.findByNomeContainingIgnoreCase("João"))
                .willReturn(List.of(usuarioExistente()));

        List<UsuarioResponseDTO> resultado = usuarioService.buscarPorNome("João");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("João Silva");
    }
}
