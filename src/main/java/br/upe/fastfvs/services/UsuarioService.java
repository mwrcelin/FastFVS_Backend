package br.upe.fastfvs.services;

import br.upe.fastfvs.entities.Usuario;

public interface UsuarioService {
    Usuario cadastrar(Usuario usuario);
    Usuario buscarPorEmail(String email);
    Usuario buscarPorId(Long id);
}