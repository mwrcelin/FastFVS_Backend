package br.upe.fastfvs.services.impl;

import br.upe.fastfvs.entities.FVS;
import br.upe.fastfvs.entities.HistoricoFVS;
import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.enums.AcaoFVS;
import br.upe.fastfvs.entities.enums.StatusFVS;
import br.upe.fastfvs.exceptions.RecursoNaoEncontradoException;
import br.upe.fastfvs.repositories.FVSRepository;
import br.upe.fastfvs.repositories.HistoricoFVSRepository;
import br.upe.fastfvs.repositories.SubsecaoRepository;
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
    private final SubsecaoRepository subsecaoRepository;

    @Override
    @Transactional
    public FVS criarFVS(FVS fvs, Usuario criador) {
        fvs.setDataAbertura(Instant.now());
        fvs.setDataUltimaEdicao(Instant.now());
        fvs.setStatus(StatusFVS.NAO_INICIADA);
        fvs.setAbertaPor(criador);

        FVS fvsSalva = fvsRepository.save(fvs);
        registrarHistorico(fvsSalva, criador, AcaoFVS.CRIACAO);
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
    public double calcularPercentualConformidadePorSubsecao(Long subsecaoId) {
        long total = fvsRepository.countBySubsecaoId(subsecaoId);
        long conformes = fvsRepository.countBySubsecaoIdAndStatus(subsecaoId, StatusFVS.CONFORME);

        if (total == 0) return 0.0;
        return (double) (conformes * 100) / total;
    }

    @Override
    public long contarFvsPorStatusEObra(Long obraId, StatusFVS status) {
        return fvsRepository.countByObraIdAndStatus(obraId, status);
    }

    @Override
    @Transactional
    public FVS atualizarStatus(UUID fvsId, StatusFVS novoStatus, Usuario usuario) {
        FVS fvs = fvsRepository.findById(fvsId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("FVS", fvsId));

        fvs.setStatus(novoStatus);
        fvs.setDataUltimaEdicao(Instant.now());
        fvs.setUltimaEdicaoPor(usuario);

        FVS fvsAtualizada = fvsRepository.save(fvs);
        registrarHistorico(fvsAtualizada, usuario, AcaoFVS.EDICAO_STATUS);

        return fvsAtualizada;
    }

    private void registrarHistorico(FVS fvs, Usuario usuario, AcaoFVS acao) {
        HistoricoFVS historico = new HistoricoFVS();
        historico.setFvs(fvs);
        historico.setUsuario(usuario);
        historico.setAcao(acao);
        historico.setDataHora(Instant.now());

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

    @Override
    @Transactional
    public List<FVS> criarFVSEmTodasSubsecoes(String titulo, Long obraId, Usuario criador) {
        List<Subsecao> subsecoes = subsecaoRepository.findByObraId(obraId);
        return subsecoes.stream().map(subsecao -> {
            FVS fvs = new FVS();
            fvs.setTitulo(titulo);
            fvs.setSubsecao(subsecao);
            return criarFVS(fvs, criador);
        }).toList();
    }

    @Override
    @Transactional
    public void deletarFVS(UUID id) {
        FVS fvs = fvsRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("FVS", id));
        fvsRepository.delete(fvs);
    }

    @Override
    public boolean existeFvsComStatusNaSubsecao(Long subsecaoId, StatusFVS status) {
        return fvsRepository.existsBySubsecaoIdAndStatus(subsecaoId, status);
    }
}