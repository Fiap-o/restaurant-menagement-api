package com.fiap.restaurant_management.dto;

import com.fiap.restaurant_management.entities.TipoUsuario;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    @Schema(description = "ID do usuário", example = "1")
    private Long id;

    @Schema(description = "Nome completo do usuário", example = "Sophia Amaral Silva")
    private String nome;

    @Schema(description = "E-mail do usuário", example = "sophia.asilva@email.com")
    private String email;

    @Schema(description = "Login utilizado para autenticação", example = "sophia.asilva")
    private String login;

    @Schema(description = "Tipo do usuário")
    private TipoUsuario tipoUsuario;

    @Schema(description = "Endereço do usuário")
    private EnderecoDTO endereco;

    @Schema(description = "Data e hora da última alteração dos dados do usuário")
    private LocalDateTime dataUltimaAlteracao;
}