package com.fiap.restaurant_management.service.impl;

import java.util.List;
import java.time.LocalDateTime;

import com.fiap.restaurant_management.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fiap.restaurant_management.entities.Usuario;
import com.fiap.restaurant_management.mapper.UsuarioMapper;
import com.fiap.restaurant_management.mapper.EnderecoMapper;
import com.fiap.restaurant_management.service.UsuarioService;
import com.fiap.restaurant_management.repositories.UsuarioRepository;
import com.fiap.restaurant_management.exception.EmailJaCadastradoException;
import com.fiap.restaurant_management.exception.LoginJaCadastradoException;
import com.fiap.restaurant_management.exception.SenhaAtualInvalidaException;
import com.fiap.restaurant_management.exception.UsuarioNaoEncontradoException;
import com.fiap.restaurant_management.exception.CredenciaisInvalidasException;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsuarioResponseDTO criar(UsuarioCreateDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail()))
            throw new EmailJaCadastradoException(dto.getEmail());
        if (usuarioRepository.findByLogin(dto.getLogin()).isPresent())
            throw new LoginJaCadastradoException(dto.getLogin());

        Usuario usuario = UsuarioMapper.toEntity(dto);
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setDataUltimaAlteracao(LocalDateTime.now());

        Usuario salvo = usuarioRepository.save(usuario);
        return UsuarioMapper.toResponseDTO(salvo);
    }

    @Override
    public UsuarioResponseDTO atualizarDados(Long id, UsuarioUpdateDTO dto) {
        Usuario usuario = buscarEntidadePorId(id);

        if (!usuario.getEmail().equals(dto.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail()))
            throw new EmailJaCadastradoException(dto.getEmail());
        if (!usuario.getLogin().equals(dto.getLogin()) && usuarioRepository.findByLogin(dto.getLogin()).isPresent())
            throw new LoginJaCadastradoException(dto.getLogin());

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setLogin(dto.getLogin());
        usuario.setEndereco(EnderecoMapper.toEntity(dto.getEndereco()));
        usuario.setDataUltimaAlteracao(LocalDateTime.now());

        Usuario atualizado = usuarioRepository.save(usuario);
        return UsuarioMapper.toResponseDTO(atualizado);
    }

    @Override
    public void atualizarSenha(Long id, SenhaUpdateDTO dto) {
        Usuario usuario = buscarEntidadePorId(id);

        if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getSenha()))
            throw new SenhaAtualInvalidaException();

        usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
        usuario.setDataUltimaAlteracao(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    @Override
    public void excluir(Long id) {
        Usuario usuario = buscarEntidadePorId(id);
        usuarioRepository.delete(usuario);
    }

    @Override
    public UsuarioResponseDTO buscarPorId(Long id) {
        return UsuarioMapper.toResponseDTO(buscarEntidadePorId(id));
    }

    @Override
    public List<UsuarioResponseDTO> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(UsuarioMapper::toResponseDTO)
                .toList();
    }

    @Override
    public boolean validarLogin(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByLogin(dto.getLogin())
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException();
        }
        return true;
    }

    private Usuario buscarEntidadePorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }
}