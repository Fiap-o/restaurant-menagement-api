package com.fiap.restaurant_management.dto;

import com.fiap.restaurant_management.entities.TipoUsuario;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String login;
    private TipoUsuario tipoUsuario;
    private EnderecoDTO endereco;
    private LocalDateTime dataUltimaAlteracao;
}