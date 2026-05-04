package br.upe.fastfvs.entities.dtos;
import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.enums.TipoSubsecao;

public record SubsecaoResponseDTO(
        Long id,
        String nome,
        TipoSubsecao tipo,
        Long obraId,
        Long paiId
) {
    public SubsecaoResponseDTO(Subsecao entity) {
        this(
                entity.getId(),
                entity.getNome(),
                entity.getTipo(),
                entity.getObra().getId(),
                entity.getPai() != null ? entity.getPai().getId() : null
        );
    }


}
