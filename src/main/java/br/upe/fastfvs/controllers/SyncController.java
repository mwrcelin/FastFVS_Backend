package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.dtos.SyncRequestDTO;
import br.upe.fastfvs.entities.dtos.SyncResponseDTO;
import br.upe.fastfvs.services.SyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sync")
@RequiredArgsConstructor
public class SyncController {

    private final SyncService syncService;

    @PostMapping
    public ResponseEntity<SyncResponseDTO> sincronizar(@RequestBody SyncRequestDTO request) {
        SyncResponseDTO response = syncService.processarSincronizacao(request);
        return ResponseEntity.ok(response);
    }
}