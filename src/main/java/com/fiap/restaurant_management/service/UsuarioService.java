package com.fiap.restaurant_management.service;

import java.util.List;

import com.fiap.restaurant_management.dto.*;

public interface UsuarioService {

    UsuarioResponseDTO criar(UsuarioCreateDTO dto);
    UsuarioResponseDTO atualizarDados(Long id, UsuarioUpdateDTO dto);
    void atualizarSenha(Long id, SenhaUpdateDTO dto);
    void excluir(Long id);
    UsuarioResponseDTO buscarPorId(Long id);
    List<UsuarioResponseDTO> buscarPorNome(String nome);
    boolean validarLogin(LoginDTO dto);
}