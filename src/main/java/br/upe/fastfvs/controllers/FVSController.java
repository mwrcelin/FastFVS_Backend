package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.FVS;
import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.*;
import br.upe.fastfvs.entities.enums.StatusFVS;
import br.upe.fastfvs.services.FVSService;
import br.upe.fastfvs.services.SubsecaoService;
import br.upe.fastfvs.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/fvs")
@RequiredArgsConstructor
public class FVSController {

    private final FVSService fvsService;
    private final UsuarioService usuarioService;
    private final SubsecaoService subsecaoService;

    @GetMapping("/padroes")
    public ResponseEntity<FVSPadroesResponseDTO> listarNomesPadroes() {
        List<String> nomes = fvsService.listarNomesPadroes();
        return ResponseEntity.ok(new FVSPadroesResponseDTO(nomes));
    }

    @GetMapping("/obra/{obraId}/conformidade")
    public ResponseEntity<ConformidadeResponseDTO> getConformidadeObra(@PathVariable Long obraId) {
        double valor = fvsService.calcularPercentualConformidade(obraId);
        return ResponseEntity.ok(new ConformidadeResponseDTO(valor));
    }

    @PostMapping
    public ResponseEntity<FVSResponseDTO> criar(
            @RequestBody @Valid FVSCreateDTO dto,
            @RequestParam Long usuarioId) {


        Usuario criador = usuarioService.buscarPorId(usuarioId);
        Subsecao subsecao = subsecaoService.buscarPorId(dto.subsecaoId());

        FVS novaFvs = dto.toEntity();
        novaFvs.setSubsecao(subsecao);

        FVS fvsSalva = fvsService.criarFVS(novaFvs, criador);

        return ResponseEntity.status(HttpStatus.CREATED).body(new FVSResponseDTO(fvsSalva));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<FVSResponseDTO> atualizarStatus(
            @PathVariable UUID id,
            @RequestBody FVSUpdateStatusDTO dto) {

        Usuario usuario = usuarioService.buscarPorId(dto.usuarioId());
        FVS fvsAtualizada = fvsService.atualizarStatus(id, dto.status(), usuario, dto.observacao());

        return ResponseEntity.ok(new FVSResponseDTO(fvsAtualizada));
    }

    @GetMapping("/subsecao/{subsecaoId}")
    public ResponseEntity<List<FVSResponseDTO>> listarPorSubsecao(@PathVariable Long subsecaoId) {
        List<FVS> fichas = fvsService.listarPorSubsecao(subsecaoId);
        List<FVSResponseDTO> dtos = fichas.stream()
                .map(FVSResponseDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/subsecao/{subsecaoId}/status/{status}")
    public ResponseEntity<List<FVSResponseDTO>> listarPorSubsecaoEStatus(
            @PathVariable Long subsecaoId,
            @PathVariable StatusFVS status) {
        List<FVSResponseDTO> dtos = fvsService.listarPorSubsecaoEStatus(subsecaoId, status)
                .stream().map(FVSResponseDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/obra/{obraId}/contagem-status")
    public ResponseEntity<Map<String, Long>> contarStatusDaObra(@PathVariable Long obraId) {
        long naoIniciadas = fvsService.contarFvsPorStatusEObra(obraId, StatusFVS.NAO_INICIADA);
        long emAnalise = fvsService.contarFvsPorStatusEObra(obraId, StatusFVS.EM_ANALISE);
        long conformes = fvsService.contarFvsPorStatusEObra(obraId, StatusFVS.CONFORME);
        long naoConformes = fvsService.contarFvsPorStatusEObra(obraId, StatusFVS.NAO_CONFORME);

        Map<String, Long> resumo = Map.of(
                "NAO_INICIADA", naoIniciadas,
                "EM_ANALISE", emAnalise,
                "CONFORME", conformes,
                "NAO_CONFORME", naoConformes
        );

        return ResponseEntity.ok(resumo);
    }
}