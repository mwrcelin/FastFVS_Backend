package br.upe.fastfvs.entities.dtos;

// vai transformar 85.5 em {"percentual": 85.5}
public record ConformidadeResponseDTO(
        double percentual
) {}
