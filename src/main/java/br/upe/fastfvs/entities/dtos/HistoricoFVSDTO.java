package br.upe.fastfvs.entities.dtos;

import br.upe.fastfvs.entities.HistoricoFVS;
import br.upe.fastfvs.entities.enums.AcaoFVS;
import java.time.Instant;
import java.util.UUID;

public record HistoricoFVSDTO(
        UUID id,
        AcaoFVS acao,
        Instant dataHora,
        String usuarioNome, // Já traz o nome para facilitar o ecrã do telemóvel
        String observacao
) {
    public HistoricoFVSDTO(HistoricoFVS entity) {
        this(
                entity.getId(),
                entity.getAcao(),
                entity.getDataHora(),
                entity.getUsuario().getNome(),
                entity.getObservacao()
        );
    }
}