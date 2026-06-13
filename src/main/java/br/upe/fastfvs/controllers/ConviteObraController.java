package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.enums.TipoPermissao;
import br.upe.fastfvs.services.ConviteObraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/convites")
@RequiredArgsConstructor
public class ConviteObraController {

    private final ConviteObraService conviteObraService;

    @PostMapping("/obra/{obraId}")
    public ResponseEntity<Map<String, String>> gerarConvite(
            @PathVariable Long obraId,
            @RequestParam TipoPermissao role) {

        String link = conviteObraService.gerarConvite(obraId, role);
        return ResponseEntity.ok(Map.of("link", link));
    }

    @PostMapping("/aceitar")
    public ResponseEntity<Void> aceitarConvite(
            @RequestParam String token,
            @RequestParam Long usuarioId) {

        conviteObraService.aceitarConvite(token, usuarioId);
        return ResponseEntity.noContent().build();
    }
}