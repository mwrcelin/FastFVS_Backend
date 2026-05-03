package br.upe.fastfvs.services.impl;

import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.repositories.UsuarioRepository;
import br.upe.fastfvs.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}