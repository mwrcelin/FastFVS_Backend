package br.upe.fastfvs.entities.dtos;

import br.upe.fastfvs.entities.FVS;

public record FVSCreateDTO(
        String titulo,
        Long subsecaoId
) {
    public FVS toEntity() {
        FVS fvs = new FVS();
        fvs.setTitulo(this.titulo());
        // Subseção será associada no Controller/Service.
        // Status, datas e UUID são gerados automaticamente.
        return fvs;
    }
}