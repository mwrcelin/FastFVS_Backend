package br.upe.fastfvs.services;

import br.upe.fastfvs.entities.dtos.SyncRequestDTO;
import br.upe.fastfvs.entities.dtos.SyncResponseDTO;

public interface SyncService {
    SyncResponseDTO processarSincronizacao(SyncRequestDTO request);
}