package br.upe.fastfvs.services;

import br.upe.fastfvs.entities.Usuario;

public interface UsuarioService {
    Usuario cadastrar(Usuario usuario);
    Usuario buscarPorEmail(String email);
    Usuario buscarPorId(Long id);
    void atualizarDados(Long id, String nome, String email);

    void atualizarSenha(Long id, String senhaAtual, String novaSenha);

    void atualizarFoto(Long id, String foto);

    void excluirConta(Long id);
}