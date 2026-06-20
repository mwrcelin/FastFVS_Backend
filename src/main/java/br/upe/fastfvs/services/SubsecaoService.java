package br.upe.fastfvs.services;

import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.NivelHierarquiaDTO;

import java.util.List;

public interface SubsecaoService {

    Subsecao criarSubsecao(Subsecao subsecao, Usuario criador, List<String> fvsEscolhidas);
    void criarEstruturaAutomatica(Long obraId, List<NivelHierarquiaDTO> niveis, Usuario criador, List<String> fvsEscolhidas);
    List<Subsecao> listarRaizPorObra(Long obraId);
    List<Subsecao> listarFilhas(Long paiId);

    Subsecao buscarPorId(Long id);
    String obterCaminhoCompleto(Long subsecaoId);
    void deletarSubsecao(Long id);
    double calcularPercentualConformidade(Long subsecaoId);
    Subsecao atualizarNome(Long id, String novoNome);
}