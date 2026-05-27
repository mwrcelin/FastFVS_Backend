package br.upe.fastfvs.entities.dtos;

import br.upe.fastfvs.entities.enums.StatusFVS;
import java.time.Instant;
import java.util.UUID;

public record FVSSyncDTO(
        UUID id,
        Long versao, // Versão que o do tem celular localmente
        StatusFVS status,
        String observacao,
        Instant dataUltimaEdicao,
        Long usuarioId
) {}