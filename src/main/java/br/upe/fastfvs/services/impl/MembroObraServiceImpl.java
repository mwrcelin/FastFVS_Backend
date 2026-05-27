package br.upe.fastfvs.services.impl;

import br.upe.fastfvs.entities.MembroObra;
import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.enums.TipoPermissao;
import br.upe.fastfvs.repositories.MembroObraRepository;
import br.upe.fastfvs.services.MembroObraService;
import br.upe.fastfvs.services.ObraService;
import br.upe.fastfvs.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembroObraServiceImpl implements MembroObraService {

    private final MembroObraRepository repository;
    private final UsuarioService usuarioService;
    private final ObraService obraService;

    @Override
    public MembroObra adicionarMembro(Obra obra, Usuario usuario, TipoPermissao role) {
        MembroObra membro = new MembroObra();
        membro.setObra(obra);
        membro.setUsuario(usuario);
        membro.setRole(role);
        return repository.save(membro);
    }

    @Override
    public MembroObra adicionarMembro(Long usuarioId, Long obraId, TipoPermissao role) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        Obra obra = obraService.buscarPorId(obraId);

        return this.adicionarMembro(obra, usuario, role);
    }

    @Override
    public List<MembroObra> listarMembrosPorObra(Long obraId) {
        return repository.findByObraId(obraId);
    }

    @Override
    public TipoPermissao buscarPermissao(Long usuarioId, Long obraId) {
        return repository.findByUsuarioIdAndObraId(usuarioId, obraId)
                .map(MembroObra::getRole)
                .orElse(null);
    }

    @Override
    public void removerMembro(Long id) {
        repository.deleteById(id);
    }

    @Override
    public MembroObra alterarRole(Long id, String novaRole) {
        MembroObra membro = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membro não encontrado"));

        TipoPermissao permissao = TipoPermissao.valueOf(novaRole.toUpperCase());
        membro.setRole(permissao);

        return repository.save(membro);
    }
}