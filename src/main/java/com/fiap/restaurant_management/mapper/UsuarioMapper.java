package com.fiap.restaurant_management.mapper;

import com.fiap.restaurant_management.entities.Usuario;
import com.fiap.restaurant_management.dto.UsuarioCreateDTO;
import com.fiap.restaurant_management.dto.UsuarioResponseDTO;

public class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static Usuario toEntity(UsuarioCreateDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setLogin(dto.getLogin());
        usuario.setSenha(dto.getSenha());
        usuario.setTipoUsuario(dto.getTipoUsuario());
        usuario.setEndereco(EnderecoMapper.toEntity(dto.getEndereco()));
        return usuario;
    }

    public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .login(usuario.getLogin())
                .tipoUsuario(usuario.getTipoUsuario())
                .endereco(EnderecoMapper.toDTO(usuario.getEndereco()))
                .dataUltimaAlteracao(usuario.getDataUltimaAlteracao())
                .build();
    }
}