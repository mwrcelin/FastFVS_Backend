package br.upe.fastfvs.services.impl;

import br.upe.fastfvs.entities.MembroObra;
import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.enums.TipoPermissao;
import br.upe.fastfvs.repositories.MembroObraRepository;
import br.upe.fastfvs.repositories.ObraRepository;
import br.upe.fastfvs.services.ObraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ObraServiceImpl implements ObraService {

    private final ObraRepository obraRepository;
    private final MembroObraRepository membroObraRepository;

    @Override
    @Transactional
    public Obra criarObra(Obra obra, Usuario criador) {
        Obra obraSalva = obraRepository.save(obra);

        MembroObra vinculo = new MembroObra();
        vinculo.setObra(obraSalva);
        vinculo.setUsuario(criador);
        vinculo.setRole(TipoPermissao.GERENTE);

        membroObraRepository.save(vinculo);

        return obraSalva;
    }

    @Override
    public List<Obra> listarObrasDoUsuario(Long usuarioId) {
        return obraRepository.findByMembrosUsuarioId(usuarioId);
    }

    @Override
    public Obra buscarPorId(Long id) {
        return obraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obra não encontrada com o ID: " + id));
    }

    @Override
    @Transactional
    public void apagarObra(Long id) {
        Obra obra = buscarPorId(id);
        obraRepository.delete(obra);
    }
}