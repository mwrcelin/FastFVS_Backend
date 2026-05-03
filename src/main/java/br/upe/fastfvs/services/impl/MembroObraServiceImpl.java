package br.upe.fastfvs.services.impl;

import br.upe.fastfvs.entities.MembroObra;
import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.enums.TipoPermissao;
import br.upe.fastfvs.repositories.MembroObraRepository;
import br.upe.fastfvs.services.MembroObraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembroObraServiceImpl implements MembroObraService {

    private final MembroObraRepository repository;

    @Override
    public MembroObra adicionarMembro(Obra obra, Usuario usuario, TipoPermissao role) {
        MembroObra membro = new MembroObra();
        membro.setObra(obra);
        membro.setUsuario(usuario);
        membro.setRole(role);
        return repository.save(membro);
    }

    @Override
    public List<MembroObra> listarMembrosPorObra(Long obraId) {
        // Nota: Você pode precisar adicionar findByObraId no seu MembroObraRepository
        return repository.findAll().stream()
                .filter(m -> m.getObra().getId().equals(obraId))
                .toList();
    }

    @Override
    public TipoPermissao buscarPermissao(Long usuarioId, Long obraId) {

        Usuario usuarioRef = new Usuario();
        usuarioRef.setId(usuarioId);

        Obra obraRef = new Obra();
        obraRef.setId(obraId);

        MembroObra vinculo = repository.findByUsuarioAndObra(usuarioRef, obraRef)
                .orElseThrow(() -> new RuntimeException("O usuário não faz parte desta obra. Acesso negado."));

        return vinculo.getRole(); // Retorna se é GERENTE ou PADRAO
    }
}