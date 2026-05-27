package br.upe.fastfvs.entities.dtos;
import br.upe.fastfvs.entities.Subsecao;

public record SubsecaoResponseDTO(
        Long id,
        String nome,
        Long obraId,
        Long paiId
) {
    public SubsecaoResponseDTO(Subsecao entity) {
        this(
                entity.getId(),
                entity.getNome(),
                entity.getObra() != null ? entity.getObra().getId() : null,
                entity.getPai() != null ? entity.getPai().getId() : null
        );
    }
}