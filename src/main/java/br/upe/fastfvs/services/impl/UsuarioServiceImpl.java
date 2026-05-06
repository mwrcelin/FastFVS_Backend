package br.upe.fastfvs.services.impl;

import br.upe.fastfvs.entities.Usuario;
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
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o e-mail: " + email));
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }

    @Override
    public void atualizarDados(Long id, String nome, String email) {
        Usuario usuario = buscarPorId(id);

        if (!usuario.getEmail().equalsIgnoreCase(email)) {
            Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(email);
            if (usuarioExistente.isPresent()) {
                throw new RuntimeException("Este email já está em uso por outra conta.");
            }
        }

        usuario.setNome(nome);
        usuario.setEmail(email);
        usuarioRepository.save(usuario);
    }

    @Override
    public void atualizarSenha(Long id, String senhaAtual, String novaSenha) {
        Usuario usuario = buscarPorId(id);
        if (!usuario.getSenha().equals(senhaAtual)) {
            throw new RuntimeException("A senha atual informada está incorreta.");
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