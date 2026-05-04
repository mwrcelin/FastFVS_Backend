package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.FVS;
import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.*;
import br.upe.fastfvs.services.FVSService;
import br.upe.fastfvs.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

        // Em vez de retornar a lista pura, retorna o DTO
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

        // Converte o DTO para Entidade
        FVS novaFvs = dto.toEntity();

        // Faz o vínculo com a subseção usando o ID que veio no DTO
        Subsecao subsecaoRef = new Subsecao();
        subsecaoRef.setId(dto.subsecaoId());
        novaFvs.setSubsecao(subsecaoRef);

        // O Service salva a entidade no banco de dados
        FVS fvsSalva = fvsService.criarFVS(novaFvs, criador);

        // Devolve o FVSResponseDTO (escondendo os dados sensíveis)
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
}