package br.upe.fastfvs.entities.dtos;

import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.enums.TipoPermissao;
import jakarta.validation.constraints.NotBlank;


public record ObraDTO(
        Long id,

        @NotBlank(message = "O nome da obra não pode estar vazio")
        String nome,

        @NotBlank(message = "O link do projeto é obrigatório")
        String linkProjeto,

        Double percentualConformidade, // Progresso (ex: 31%)
        TipoPermissao role,            // GERENTE ou PADRÃO
        Long usuarioId                 // ID do utilizador (criador ou logado)
) {

    public ObraDTO(Obra entity, Double percentualConformidade, TipoPermissao role) {
        this(
                entity.getId(),
                entity.getNome(),
                entity.getLinkProjeto(),
                percentualConformidade,
                role,
                null
        );
    }


    public ObraDTO(Obra entity) {
        this(entity.getId(), entity.getNome(), entity.getLinkProjeto(), null, null, null);
    }

    public Obra toEntity() {
        Obra obra = new Obra();
        obra.setId(this.id());
        obra.setNome(this.nome());
        obra.setLinkProjeto(this.linkProjeto());
        return obra;
    }
}