package com.fiap.restaurant_management.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @Schema(description = "Login do usuário", example = "maria.silva")
    @NotBlank(message = "Login é obrigatório")
    private String login;

    @Schema(description = "Senha do usuário", example = "SenhaForte123")
    @NotBlank(message = "Senha é obrigatória")
    private String senha;
}