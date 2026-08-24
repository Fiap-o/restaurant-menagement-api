package com.fiap.restaurant_management.dto;

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
public class UsuarioUpdateDTO {

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

    @Schema(description = "Endereço do usuário")
    @Valid
    @NotNull(message = "Endereço é obrigatório")
    private EnderecoDTO endereco;
}