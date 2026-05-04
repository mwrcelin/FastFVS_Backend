package br.upe.fastfvs.entities.dtos;

import br.upe.fastfvs.entities.Usuario;

public record UsuarioCreateDTO(
        String nome,
        String email,
        String senha,
        String fotoPerfil
) {
    public Usuario toEntity() {
        Usuario usuario = new Usuario();
        usuario.setNome(this.nome());
        usuario.setEmail(this.email());
        usuario.setSenha(this.senha());
        usuario.setFotoPerfil(this.fotoPerfil());
        return usuario;
    }
}