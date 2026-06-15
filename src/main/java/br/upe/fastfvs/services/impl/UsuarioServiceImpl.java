package br.upe.fastfvs.services.impl;

import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.exceptions.EmailJaCadastradoException;
import br.upe.fastfvs.exceptions.RecursoNaoEncontradoException;
import br.upe.fastfvs.exceptions.SenhaIncorretaException;
import br.upe.fastfvs.repositories.UsuarioRepository;
import br.upe.fastfvs.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public Usuario cadastrar(Usuario usuario) {

        usuarioRepository.findByEmail(usuario.getEmail()).ifPresent(u -> {
            throw new EmailJaCadastradoException(usuario.getEmail());
        });
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", email));
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
    }

    @Override
    public void atualizarDados(Long id, String nome, String email) {
        Usuario usuario = buscarPorId(id);


        if (!usuario.getEmail().equalsIgnoreCase(email)) {
            usuarioRepository.findByEmail(email).ifPresent(u -> {
                throw new EmailJaCadastradoException(email);
            });
        }

        usuario.setNome(nome);
        usuario.setEmail(email);
        usuarioRepository.save(usuario);
    }

    @Override
    public void atualizarSenha(Long id, String senhaAtual, String novaSenha) {
        Usuario usuario = buscarPorId(id);
        if (!usuario.getSenha().equals(senhaAtual)) {
            throw new SenhaIncorretaException();
        }

        usuario.setSenha(novaSenha);
        usuarioRepository.save(usuario);
    }

    @Override
    public void atualizarFoto(Long id, String fotoBase64ouUrl) {
        Usuario usuario = buscarPorId(id);
        usuario.setFotoPerfil(fotoBase64ouUrl); 
        usuarioRepository.save(usuario);
    }

    @Override
    public void excluirConta(Long id) {
        Usuario usuario = buscarPorId(id);

        usuarioRepository.delete(usuario);
    }
}