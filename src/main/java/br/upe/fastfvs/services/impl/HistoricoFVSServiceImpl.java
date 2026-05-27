package br.upe.fastfvs.services.impl;

import br.upe.fastfvs.entities.HistoricoFVS;
import br.upe.fastfvs.repositories.HistoricoFVSRepository;
import br.upe.fastfvs.services.HistoricoFVSService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HistoricoFVSServiceImpl implements HistoricoFVSService {

    private final HistoricoFVSRepository repository;

    @Override
    public void registrar(HistoricoFVS historico) {
        repository.save(historico);
    }

    @Override
    public List<HistoricoFVS> listarPorFicha(UUID fvsId) {
        return repository.findByFvsIdOrderByDataHoraDesc(fvsId);
    }
}