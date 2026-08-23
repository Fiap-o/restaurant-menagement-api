package com.fiap.restaurant_management.mapper;

import com.fiap.restaurant_management.dto.EnderecoDTO;
import com.fiap.restaurant_management.entities.Endereco;

public class EnderecoMapper {

    private EnderecoMapper() {
    }

    public static Endereco toEntity(EnderecoDTO dto) {
        if (dto == null) {
            return null;
        }
        Endereco endereco = new Endereco();
        endereco.setRua(dto.getRua());
        endereco.setNumero(dto.getNumero());
        endereco.setCidade(dto.getCidade());
        endereco.setCep(dto.getCep());
        return endereco;
    }

    public static EnderecoDTO toDTO(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        return new EnderecoDTO(
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getCidade(),
                endereco.getCep()
        );
    }
}