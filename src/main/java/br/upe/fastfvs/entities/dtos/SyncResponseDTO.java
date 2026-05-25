package br.upe.fastfvs.entities.dtos;

import java.util.List;
import java.util.UUID;

public record SyncResponseDTO(
        List<UUID> sucessos,
        List<FVSResponseDTO> conflitos
) {}