package br.upe.fastfvs.entities.dtos;

import br.upe.fastfvs.entities.FVS;
import br.upe.fastfvs.entities.enums.StatusFVS;
import java.time.Instant;
import java.util.UUID;

public record FVSResponseDTO(
        UUID id,
        Long versao,
        String titulo,
        StatusFVS status,
        Instant dataAbertura,
        Instant dataUltimaEdicao,
        Long subsecaoId,
        Long abertaPorId
) {
    
    public FVSResponseDTO(FVS entity) {
        this(
                entity.getId(),
                entity.getVersao(),
                entity.getTitulo(),
                entity.getStatus(),
                entity.getDataAbertura(),
                entity.getDataUltimaEdicao(),
                entity.getSubsecao() != null ? entity.getSubsecao().getId() : null,
                entity.getAbertaPor() != null ? entity.getAbertaPor().getId() : null
        );
    }
}