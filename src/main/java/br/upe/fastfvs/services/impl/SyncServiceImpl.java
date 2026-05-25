package br.upe.fastfvs.services.impl;

import br.upe.fastfvs.entities.FVS;
import br.upe.fastfvs.entities.HistoricoFVS;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.FVSSyncDTO;
import br.upe.fastfvs.entities.dtos.FVSResponseDTO;
import br.upe.fastfvs.entities.dtos.SyncRequestDTO;
import br.upe.fastfvs.entities.dtos.SyncResponseDTO;
import br.upe.fastfvs.entities.enums.AcaoFVS;
import br.upe.fastfvs.repositories.FVSRepository;
import br.upe.fastfvs.repositories.HistoricoFVSRepository;
import br.upe.fastfvs.services.SyncService;
import br.upe.fastfvs.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SyncServiceImpl implements SyncService {

    private final FVSRepository fvsRepository;
    private final HistoricoFVSRepository historicoRepository;
    private final UsuarioService usuarioService;

    @Override
    @Transactional
    public SyncResponseDTO processarSincronizacao(SyncRequestDTO request) {
        List<UUID> sucessos = new ArrayList<>();
        List<FVSResponseDTO> conflitos = new ArrayList<>();

        for (FVSSyncDTO item : request.alteracoes()) {
            Optional<FVS> fvsOpt = fvsRepository.findById(item.id());

            if (fvsOpt.isPresent()) {
                FVS fvsBanco = fvsOpt.get();

                // COMPARAÇÃO DE VERSÕES (A mágica do Sync)
                if (fvsBanco.getVersao().equals(item.versao())) {
                    // Versões coincidem: Podemos atualizar
                    atualizarFVS(fvsBanco, item);
                    sucessos.add(item.id());
                } else {
                    // Versões diferentes: Alguém editou antes na nuvem
                    conflitos.add(new FVSResponseDTO(fvsBanco));
                }
            }
        }

        return new SyncResponseDTO(sucessos, conflitos);
    }

    private void atualizarFVS(FVS fvs, FVSSyncDTO item) {
        Usuario usuario = usuarioService.buscarPorId(item.usuarioId());

        fvs.setStatus(item.status());
        fvs.setDataUltimaEdicao(item.dataUltimaEdicao());
        fvs.setUltimaEdicaoPor(usuario);

        // Salva e incrementa a versão automaticamente (@Version)
        FVS fvsSalva = fvsRepository.save(fvs);

        // Registra no histórico
        registrarHistorico(fvsSalva, usuario, item.observacao());
    }

    private void registrarHistorico(FVS fvs, Usuario usuario, String obs) {
        HistoricoFVS historico = new HistoricoFVS();
        historico.setFvs(fvs);
        historico.setUsuario(usuario);
        historico.setAcao(AcaoFVS.EDICAO_STATUS);
        historico.setDataHora(fvs.getDataUltimaEdicao());
        historico.setObservacao(obs);
        historicoRepository.save(historico);
    }
}