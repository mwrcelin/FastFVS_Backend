package br.upe.fastfvs.entities.dtos;

import br.upe.fastfvs.entities.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String fotoPerfil
) {
    public UsuarioResponseDTO(Usuario entity) {
        this(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getFotoPerfil()
        );
    }
}