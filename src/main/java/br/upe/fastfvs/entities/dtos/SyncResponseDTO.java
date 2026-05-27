package br.upe.fastfvs.entities.dtos;

import java.util.List;

public record SyncResponseDTO(

        List<FVSResponseDTO> sucessos,
        List<FVSResponseDTO> conflitos
) {}