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
public class EnderecoDTO {

    @Schema(description = "Nome da rua", example = "Rua das Flores")
    @NotBlank(message = "Rua é obrigatória")
    private String rua;

    @Schema(description = "Número do imóvel", example = "123")
    @NotBlank(message = "Número é obrigatório")
    private String numero;

    @Schema(description = "Cidade", example = "São Paulo")
    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;

    @Schema(description = "CEP", example = "01310-100")
    @NotBlank(message = "CEP é obrigatório")
    private String cep;
}