package br.upe.fastfvs.entities.dtos;


import br.upe.fastfvs.entities.MembroObra;
import br.upe.fastfvs.entities.enums.TipoPermissao;

public record MembroObraDTO(
        Long id,
        Long usuarioId,
        Long obraId,
        TipoPermissao role
) {
    // Transcreve Entidade -> DTO
    public MembroObraDTO(MembroObra entity) {
        this(
                entity.getId(),
                entity.getUsuario().getId(),
                entity.getObra().getId(),
                entity.getRole()
        );
    }

    // Transcreve DTO -> Entidade
    public MembroObra toEntity() {
        MembroObra membro = new MembroObra();
        membro.setId(this.id());
        membro.setRole(this.role());

        return membro;
    }
}
