package br.upe.fastfvs.services;

import br.upe.fastfvs.entities.HistoricoFVS;
import java.util.List;
import java.util.UUID;

public interface HistoricoFVSService {
    void registrar(HistoricoFVS historico);
    List<HistoricoFVS> listarPorFicha(UUID fvsId);
}