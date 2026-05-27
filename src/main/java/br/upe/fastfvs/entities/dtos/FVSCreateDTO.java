package br.upe.fastfvs.entities.dtos;

import br.upe.fastfvs.entities.FVS;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FVSCreateDTO(
        @NotBlank(message = "O título da FVS é obrigatório")
        String titulo,

        @NotNull(message = "O ID da subsecção é obrigatório")
        Long subsecaoId
) {
    public FVS toEntity() {
        FVS fvs = new FVS();
        fvs.setTitulo(this.titulo());
        return fvs;
    }
}