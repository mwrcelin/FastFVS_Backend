package br.upe.fastfvs.services;

import br.upe.fastfvs.entities.FVS;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.enums.StatusFVS;
import java.util.UUID;
import java.util.List;

public interface FVSService {

    FVS criarFVS(FVS fvs, Usuario criador);
    FVS atualizarStatus(UUID fvsId, StatusFVS novoStatus, Usuario usuario);
    long contarFvsPorStatusEObra(Long obraId, StatusFVS status);

    double calcularPercentualConformidade(Long obraId);
    List<String> listarNomesPadroes();
    List<FVS> listarPorSubsecao(Long subsecaoId);
    List<FVS> listarPorSubsecaoEStatus(Long subsecaoId, StatusFVS status);
    List<FVS> criarFVSEmTodasSubsecoes(String titulo, Long obraId, Usuario criador);
    void deletarFVS(UUID id);
    boolean existeFvsComStatusNaSubsecao(Long subsecaoId, StatusFVS status);
    double calcularPercentualConformidadePorSubsecao(Long subsecaoId);
    int deletarPorTituloNaObra(Long obraId, String titulo);
}