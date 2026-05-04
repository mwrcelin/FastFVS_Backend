package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.SubsecaoCreateDTO;
import br.upe.fastfvs.entities.dtos.SubsecaoResponseDTO;
import br.upe.fastfvs.services.SubsecaoService;
import br.upe.fastfvs.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subsecao")
@RequiredArgsConstructor
public class SubsecaoController {

    private final SubsecaoService subsecaoService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<SubsecaoResponseDTO> criar(@RequestBody SubsecaoCreateDTO dto) {
        Usuario criador = usuarioService.buscarPorId(dto.usuarioId()); // Exemplo
        Subsecao novaSubsecao = dto.toEntity();

        // Passa a lista que veio do DTO (que o usuário selecionou no App)
        Subsecao salva = subsecaoService.criarSubsecao(novaSubsecao, criador, dto.fvsEscolhidas());

        return ResponseEntity.ok(new SubsecaoResponseDTO(salva));
    }
}
