package br.upe.fastfvs.entities.dtos;

import java.util.List;

public record SyncRequestDTO(
        List<FVSSyncDTO> alteracoes
) {}