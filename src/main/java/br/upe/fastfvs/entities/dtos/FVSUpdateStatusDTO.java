package br.upe.fastfvs.entities.dtos;

import br.upe.fastfvs.entities.enums.StatusFVS;

public record FVSUpdateStatusDTO(
        StatusFVS status,
        Long usuarioId,
        String observacao
) {}