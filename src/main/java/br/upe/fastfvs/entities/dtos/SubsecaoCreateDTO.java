package br.upe.fastfvs.entities.dtos;

import br.upe.fastfvs.entities.Subsecao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SubsecaoCreateDTO(
        @NotBlank(message = "O nome da subsecção é obrigatório")
        String nome,

        @NotNull(message = "O ID da obra é obrigatório")
        Long obraId,

        Long paiId, // Opcional

        @NotNull(message = "O ID do usuário é obrigatório")
        Long usuarioId,

        List<String> fvsEscolhidas
) {
    public Subsecao toEntity() {
        Subsecao subsecao = new Subsecao();
        subsecao.setNome(this.nome());
        return subsecao;
    }
}