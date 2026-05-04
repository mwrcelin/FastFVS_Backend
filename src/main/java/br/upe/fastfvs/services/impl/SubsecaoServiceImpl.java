package br.upe.fastfvs.services.impl;

import br.upe.fastfvs.entities.FVS;
import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.repositories.SubsecaoRepository;
import br.upe.fastfvs.services.FVSService;
import br.upe.fastfvs.services.SubsecaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubsecaoServiceImpl implements SubsecaoService {

    private final SubsecaoRepository repository;
    private final FVSService fvsService; // Injetado para criar as FVS com histórico


    @Override
    @Transactional
    public Subsecao criarSubsecao(Subsecao subsecao, Usuario criador, List<String> fvsEscolhidas) {
        Subsecao salva = repository.save(subsecao);

        if (fvsEscolhidas != null && !fvsEscolhidas.isEmpty()) {
            for (String titulo : fvsEscolhidas) {
                FVS novaFvs = new FVS();
                novaFvs.setTitulo(titulo);
                novaFvs.setSubsecao(salva);
                fvsService.criarFVS(novaFvs, criador);
            }
        }
        return salva;
    }

    @Override
    public List<Subsecao> listarRaizPorObra(Long obraId) {
        // referência da obra apenas com o ID para a consulta
        Obra obra = new Obra();
        obra.setId(obraId);
        return repository.findByObraAndPaiIsNull(obra);
    }

    @Override
    public List<Subsecao> listarFilhas(Long paiId) {
        return repository.findByPaiId(paiId);
    }
}