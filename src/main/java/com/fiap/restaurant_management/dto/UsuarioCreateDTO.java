package com.fiap.restaurant_management.dto;

import com.fiap.restaurant_management.entities.TipoUsuario;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCreateDTO {

    @Schema(description = "Nome completo do usuário", example = "Sophia Amaral Silva")
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Schema(description = "E-mail do usuário", example = "sophia.asilva@email.com")
    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @Schema(description = "Login utilizado para autenticação", example = "sophia.asilva")
    @NotBlank(message = "Login é obrigatório")
    private String login;

    @Schema(description = "Senha do usuário", example = "SenhaFort387542")
    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    @Schema(description = "Tipo do usuário")
    @NotNull(message = "Tipo de usuário é obrigatório")
    private TipoUsuario tipoUsuario;

    @Schema(description = "Endereço do usuário")
    @NotNull(message = "Endereço é obrigatório")
    @Valid
    private EnderecoDTO endereco;
}