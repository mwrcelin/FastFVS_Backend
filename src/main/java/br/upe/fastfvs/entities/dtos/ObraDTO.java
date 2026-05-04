package br.upe.fastfvs.entities.dtos;

import br.upe.fastfvs.entities.Obra;

public record ObraDTO(
        Long id,
        String nome,
        String linkProjeto
) {
    //Transcreve Entidade -> DTO (Para responder ao telemóvel)
    public ObraDTO(Obra entity) {
        this(entity.getId(), entity.getNome(), entity.getLinkProjeto());
    }

    // Transcreve DTO -> Entidade (Para salvar na base de dados)
    public Obra toEntity() {
        Obra obra = new Obra();
        obra.setId(this.id());
        obra.setNome(this.nome());
        obra.setLinkProjeto(this.linkProjeto());
        return obra;
    }
}