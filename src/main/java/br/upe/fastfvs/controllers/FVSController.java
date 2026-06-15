package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.FVS;
import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.*;
import br.upe.fastfvs.entities.enums.StatusFVS;
import br.upe.fastfvs.exceptions.OperacaoInvalidaException;
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

    //services feitos no front, exceto as partes que vão pra obra

    private final FVSService fvsService;
    private final UsuarioService usuarioService;
    private final SubsecaoService subsecaoService;

    @GetMapping("/padroes")
    public ResponseEntity<FVSPadroesResponseDTO> listarNomesPadroes() {
        List<String> nomes = fvsService.listarNomesPadroes();
        return ResponseEntity.ok(new FVSPadroesResponseDTO(nomes));
    }

    @PostMapping
    public ResponseEntity<?> criar(
            @RequestBody @Valid FVSCreateDTO dto,
            @RequestParam Long usuarioId) {

        Usuario criador = usuarioService.buscarPorId(usuarioId);

        if (Boolean.TRUE.equals(dto.aplicarEmTodas())) {
            if (dto.obraId() == null)
                throw new OperacaoInvalidaException("obraId é obrigatório quando aplicarEmTodas = true.");

            List<FVSResponseDTO> criadas = fvsService
                    .criarFVSEmTodasSubsecoes(dto.titulo(), dto.obraId(), criador)
                    .stream().map(FVSResponseDTO::new).toList();

            return ResponseEntity.status(HttpStatus.CREATED).body(criadas);
        }

        if (dto.subsecaoId() == null)
            throw new OperacaoInvalidaException("subsecaoId é obrigatório quando aplicarEmTodas = false.");

        Subsecao subsecao = subsecaoService.buscarPorId(dto.subsecaoId());
        FVS novaFvs = dto.toEntity();
        novaFvs.setSubsecao(subsecao);

        return ResponseEntity.status(HttpStatus.CREATED).body(new FVSResponseDTO(fvsService.criarFVS(novaFvs, criador)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<FVSResponseDTO> atualizarStatus(
            @PathVariable UUID id,
            @RequestBody FVSUpdateStatusDTO dto) {

        Usuario usuario = usuarioService.buscarPorId(dto.usuarioId());
        FVS fvsAtualizada = fvsService.atualizarStatus(id, dto.status(), usuario);

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
//inútil
    @GetMapping("/subsecao/{subsecaoId}/status/{status}")
    public ResponseEntity<List<FVSResponseDTO>> listarPorSubsecaoEStatus(
            @PathVariable Long subsecaoId,
            @PathVariable StatusFVS status) {
        List<FVSResponseDTO> dtos = fvsService.listarPorSubsecaoEStatus(subsecaoId, status)
                .stream().map(FVSResponseDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFVS(@PathVariable UUID id) {
        fvsService.deletarFVS(id);
        return ResponseEntity.noContent().build();
    }

}