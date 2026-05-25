package br.upe.fastfvs.entities.dtos;

import br.upe.fastfvs.entities.Subsecao;

import java.util.List;

public record SubsecaoCreateDTO(
        String nome,
        Long obraId,
        Long paiId,
        Long usuarioId, //
        List<String> fvsEscolhidas //
) {
    public Subsecao toEntity() {
        Subsecao subsecao = new Subsecao();
        subsecao.setNome(this.nome());
        return subsecao;
    }
}