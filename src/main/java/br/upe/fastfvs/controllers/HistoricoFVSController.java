package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.HistoricoFVS;
import br.upe.fastfvs.entities.dtos.HistoricoFVSDTO;
import br.upe.fastfvs.services.HistoricoFVSService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/historico-fvs")
@RequiredArgsConstructor
public class HistoricoFVSController {

    private final HistoricoFVSService historicoService;

    @GetMapping("/ficha/{fvsId}")
    public ResponseEntity<List<HistoricoFVSDTO>> listarPorFicha(@PathVariable UUID fvsId) {
        List<HistoricoFVS> historico = historicoService.listarPorFicha(fvsId);

        List<HistoricoFVSDTO> dtos = historico.stream()
                .map(HistoricoFVSDTO::new)
                .toList();

        return ResponseEntity.ok(dtos);
    }
}