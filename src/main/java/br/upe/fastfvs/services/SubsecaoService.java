package br.upe.fastfvs.services;

import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.Usuario;
import java.util.List;

public interface SubsecaoService {

    Subsecao criarSubsecao(Subsecao subsecao, Usuario criador, List<String> fvsEscolhidas);
    void criarEstruturaAutomatica(Long obraId, int qtdBlocos, int pavPorBloco, int aptPorPav, String padraoNumeracao, Usuario criador, List<String> fvsEscolhidas);
    List<Subsecao> listarRaizPorObra(Long obraId);
    List<Subsecao> listarFilhas(Long paiId);

    Subsecao buscarPorId(Long id);
    String obterCaminhoCompleto(Long subsecaoId);
}