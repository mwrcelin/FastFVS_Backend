package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.MembroObra;
import br.upe.fastfvs.entities.dtos.MembroObraDTO;
import br.upe.fastfvs.services.MembroObraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/membros")
@RequiredArgsConstructor
public class MembroObraController {

    private final MembroObraService membroObraService;


    @PostMapping
    public ResponseEntity<MembroObraDTO> adicionarMembro(@RequestBody MembroObraDTO dto) {
        MembroObra membro = membroObraService.adicionarMembro(dto.usuarioId(), dto.obraId(), dto.role());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MembroObraDTO(membro));
    }


    @GetMapping("/obra/{obraId}")
    public ResponseEntity<List<MembroObraDTO>> listarMembrosPorObra(@PathVariable Long obraId) {
        List<MembroObra> membros = membroObraService.listarMembrosPorObra(obraId);

        List<MembroObraDTO> dtos = membros.stream()
                .map(MembroObraDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerMembro(@PathVariable Long id) {
        membroObraService.removerMembro(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<MembroObraDTO> alterarRole(@PathVariable Long id, @RequestParam String novaRole) {
        MembroObra atualizado = membroObraService.alterarRole(id, novaRole);
        return ResponseEntity.ok(new MembroObraDTO(atualizado));
    }
}