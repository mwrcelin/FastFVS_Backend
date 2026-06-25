package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.MembroObra;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.MembroObraDTO;
import br.upe.fastfvs.entities.enums.TipoPermissao;
import br.upe.fastfvs.exceptions.OperacaoInvalidaException;
import br.upe.fastfvs.repositories.MembroObraRepository;
import br.upe.fastfvs.services.MembroObraService;
import br.upe.fastfvs.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

record AdicionarMembroPorEmailDTO(String email, TipoPermissao role) {}

@RestController
@RequestMapping("/api/membros")
@RequiredArgsConstructor
public class MembroObraController {

    private final MembroObraService membroObraService;
    private final UsuarioService usuarioService;
    private final MembroObraRepository membroObraRepository;


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

    // Adiciona dentro da classe MembroObraController
    @PostMapping("/obra/{obraId}/por-email")
    public ResponseEntity<MembroObraDTO> adicionarMembroPorEmail(
            @PathVariable Long obraId,
            @RequestBody AdicionarMembroPorEmailDTO dto) {

        Usuario usuario = usuarioService.buscarPorEmail(dto.email());

        boolean jaMembro = membroObraRepository
                .findByUsuarioIdAndObraId(usuario.getId(), obraId)
                .isPresent();

        if (jaMembro) {
            throw new OperacaoInvalidaException("Este usuário já é membro desta obra.");
        }

        MembroObra membro = membroObraService.adicionarMembro(usuario.getId(), obraId, dto.role());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MembroObraDTO(membro));
    }
}