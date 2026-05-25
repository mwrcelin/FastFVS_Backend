package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.FVS;
import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.*;
import br.upe.fastfvs.entities.enums.StatusFVS;
import br.upe.fastfvs.services.FVSService;
import br.upe.fastfvs.services.UsuarioService;
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

    @GetMapping("/padroes")
    public ResponseEntity<FVSPadroesResponseDTO> listarNomesPadroes() {
        List<String> nomes = fvsService.listarNomesPadroes();

        return ResponseEntity.ok(new FVSPadroesResponseDTO(nomes));
    }

    @GetMapping("/obra/{obraId}/conformidade")
    public ResponseEntity<ConformidadeResponseDTO> getConformidadeObra(@PathVariable Long obraId) {
        double valor = fvsService.calcularPercentualConformidade(obraId);

        // Em vez de retornar o Double puro, retorna o DTO
        return ResponseEntity.ok(new ConformidadeResponseDTO(valor));
    }

    @PostMapping
    public ResponseEntity<FVSResponseDTO> criar(
            @RequestBody FVSCreateDTO dto,
            @RequestParam Long usuarioId) { // usuarioId na URL, o resto no JSON

        Usuario criador = usuarioService.buscarPorId(usuarioId);

        //Converte o DTO para Entidade
        FVS novaFvs = dto.toEntity();

        //faz o vínculo com a subseção usando o ID que veio no DTO
        Subsecao subsecaoRef = new Subsecao();
        subsecaoRef.setId(dto.subsecaoId());
        novaFvs.setSubsecao(subsecaoRef);

        //service salva a entidade no banco de dados
        FVS fvsSalva = fvsService.criarFVS(novaFvs, criador);

        //devolve o FVSResponseDTO (escondendo os dados sensíveis)
        return ResponseEntity.status(HttpStatus.CREATED).body(new FVSResponseDTO(fvsSalva));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<FVSResponseDTO> atualizarStatus(
            @PathVariable UUID id,
            @RequestBody FVSUpdateStatusDTO dto) {

        Usuario usuario = usuarioService.buscarPorId(dto.usuarioId());

        // Passa as informações desempacotadas do DTO para o Service
        FVS fvsAtualizada = fvsService.atualizarStatus(id, dto.status(), usuario, dto.observacao());

        // Devolve o FVSResponseDTO atualizado
        return ResponseEntity.ok(new FVSResponseDTO(fvsAtualizada));
    }

    @GetMapping("/subsecao/{subsecaoId}")
    public ResponseEntity<List<FVSResponseDTO>> listarPorSubsecao(@PathVariable Long subsecaoId) {
        List<FVS> fichas = fvsService.listarPorSubsecao(subsecaoId);

        // Converte a lista de entidades para DTOs usando o construtor que você já definiu
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

        // Pega o total geral (opcional, mas bom ter)
        long total = fvsService.contarFvsPorStatusEObra(obraId, null); // Se você quiser o total, pode usar o fvsRepository.countBySubsecaoObraId(obraId) que vc já tem!

        // Conta cada um dos status
        long naoIniciadas = fvsService.contarFvsPorStatusEObra(obraId, StatusFVS.NAO_INICIADA);
        long emAnalise = fvsService.contarFvsPorStatusEObra(obraId, StatusFVS.EM_ANALISE);
        long conformes = fvsService.contarFvsPorStatusEObra(obraId, StatusFVS.CONFORME);
        long naoConformes = fvsService.contarFvsPorStatusEObra(obraId, StatusFVS.NAO_CONFORME);

        // Monta um Map (que vira um JSON bonitinho no front)
        Map<String, Long> resumo = Map.of(
                "NAO_INICIADA", naoIniciadas,
                "EM_ANÁLISE", emAnalise,
                "CONFORME", conformes,
                "NAO_CONFORME", naoConformes
        );

        return ResponseEntity.ok(resumo);
    }
}