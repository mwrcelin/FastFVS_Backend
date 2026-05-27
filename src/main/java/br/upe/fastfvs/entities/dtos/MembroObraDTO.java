package br.upe.fastfvs.entities.dtos;

import br.upe.fastfvs.entities.MembroObra;
import br.upe.fastfvs.entities.enums.TipoPermissao;

public record MembroObraDTO(
        Long id,
        Long usuarioId,
        String usuarioNome,
        Long obraId,
        TipoPermissao role
) {

    public MembroObraDTO(MembroObra entity) {
        this(
                entity.getId(),
                entity.getUsuario().getId(),
                entity.getUsuario() != null ? entity.getUsuario().getNome() : "Usuário desconhecido",
                entity.getObra().getId(),
                entity.getRole()
        );
    }


    public MembroObra toEntity() {
        MembroObra membro = new MembroObra();
        membro.setId(this.id());
        membro.setRole(this.role());
        return membro;
    }
}