package br.upe.fastfvs.services;

import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.Usuario;
import java.util.List;

public interface SubsecaoService {
    // Atualizado para suportar a regra de negócio de criação automática
    Subsecao criarSubsecao(Subsecao subsecao, Usuario criador, List<String> fvsEscolhidas);

    List<Subsecao> listarRaizPorObra(Long obraId);
    List<Subsecao> listarFilhas(Long paiId);
}