package br.upe.fastfvs.entities.dtos;

import java.util.List;

// vai transformar ["A", "B"] em {"padroes": ["A", "B"]}
public record FVSPadroesResponseDTO(
        List<String> padroes
) {}