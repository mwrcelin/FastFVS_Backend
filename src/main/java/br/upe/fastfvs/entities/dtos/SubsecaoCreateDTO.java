package br.upe.fastfvs.entities.dtos;

import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.enums.TipoSubsecao;
import java.util.List;

public record SubsecaoCreateDTO(
        String nome,
        TipoSubsecao tipo,
        Long obraId,
        Long paiId,
        Long usuarioId, //
        List<String> fvsEscolhidas //
) {
    public Subsecao toEntity() {
        Subsecao subsecao = new Subsecao();
        subsecao.setNome(this.nome());
        subsecao.setTipo(this.tipo());
        return subsecao;
    }
}