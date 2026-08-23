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
public class SenhaUpdateDTO {

    @Schema(description = "Senha atual do usuário", example = "SenhaForte123")
    @NotBlank(message = "Senha atual é obrigatória")
    private String senhaAtual;

    @Schema(description = "Nova senha do usuário", example = "NovaSenhaForte456")
    @NotBlank(message = "Nova senha é obrigatória")
    private String novaSenha;
}