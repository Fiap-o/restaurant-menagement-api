package com.fiap.restaurant_management.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "Login é obrigatório")
    private String login;

    @Valid
    @NotNull(message = "Endereço é obrigatório")
    private EnderecoDTO endereco;
}