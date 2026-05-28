package br.upe.fastfvs.services.impl;

import br.upe.fastfvs.entities.FVS;
import br.upe.fastfvs.entities.HistoricoFVS;
import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.enums.AcaoFVS;
import br.upe.fastfvs.entities.enums.StatusFVS;
import br.upe.fastfvs.repositories.FVSRepository;
import br.upe.fastfvs.repositories.HistoricoFVSRepository;
import br.upe.fastfvs.services.FVSService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FVSServiceImpl implements FVSService {

    private final FVSRepository fvsRepository;
    private final HistoricoFVSRepository historicoRepository;

    @Override
    @Transactional
    public FVS criarFVS(FVS fvs, Usuario criador) {
        fvs.setDataAbertura(Instant.now());
        fvs.setDataUltimaEdicao(Instant.now());
        fvs.setStatus(StatusFVS.NAO_INICIADA);
        fvs.setAbertaPor(criador);

        FVS fvsSalva = fvsRepository.save(fvs);
        registrarHistorico(fvsSalva, criador, AcaoFVS.CRIACAO, "FVS criada via sistema.");
        return fvsSalva;
    }

    @Override
    public double calcularPercentualConformidade(Long obraId) {
        long total = fvsRepository.countBySubsecaoObraId(obraId);
        long conformes = fvsRepository.countByObraIdAndStatus(obraId, StatusFVS.CONFORME);

        if (total == 0) return 0.0;
        return (double) (conformes * 100) / total;
    }

    @Override
    public long contarFvsPorStatusEObra(Long obraId, StatusFVS status) {
        return fvsRepository.countByObraIdAndStatus(obraId, status);
    }

    @Override
    @Transactional
    public FVS atualizarStatus(UUID fvsId, StatusFVS novoStatus, Usuario usuario, String observacao) {
        FVS fvs = fvsRepository.findById(fvsId)
                .orElseThrow(() -> new RuntimeException("FVS não encontrada"));

        fvs.setStatus(novoStatus);
        fvs.setDataUltimaEdicao(Instant.now());
        fvs.setUltimaEdicaoPor(usuario);

        FVS fvsAtualizada = fvsRepository.save(fvs);
        registrarHistorico(fvsAtualizada, usuario, AcaoFVS.EDICAO_STATUS, observacao);

        return fvsAtualizada;
    }

    private void registrarHistorico(FVS fvs, Usuario usuario, AcaoFVS acao, String obs) {
        HistoricoFVS historico = new HistoricoFVS();
        historico.setFvs(fvs);
        historico.setUsuario(usuario);
        historico.setAcao(acao);
        historico.setDataHora(Instant.now());
        historico.setObservacao(obs);

        historicoRepository.save(historico);
    }

    @Override
    public List<String> listarNomesPadroes() {
        return List.of(
                "Hidráulica", "Azulejo", "Pintura",
                "Instalação Elétrica", "Impermeabilização"
        );
    }

    @Override
    public List<FVS> listarPorSubsecao(Long subsecaoId) {
        return fvsRepository.findBySubsecaoId(subsecaoId);
    }

    @Override
    public List<FVS> listarPorSubsecaoEStatus(Long subsecaoId, StatusFVS status) {
        return fvsRepository.findBySubsecaoIdAndStatus(subsecaoId, status);
    }
}