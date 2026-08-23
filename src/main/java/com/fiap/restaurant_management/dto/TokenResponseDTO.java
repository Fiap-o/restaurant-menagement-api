package com.fiap.restaurant_management.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDTO {

    @Schema(description = "Token JWT gerado para o usuário autenticado", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXJpYS5zaWx2YSJ9.4f3a1b2c...")
    private String token;

    @Schema(description = "Tipo do token", example = "Bearer")
    private String tipo;
}