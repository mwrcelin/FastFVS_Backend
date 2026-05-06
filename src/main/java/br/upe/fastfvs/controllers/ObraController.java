package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.ObraDTO;
import br.upe.fastfvs.services.ObraService;
import br.upe.fastfvs.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/obras")
@RequiredArgsConstructor
public class ObraController {

    private final ObraService obraService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<ObraDTO> criarObra(@RequestBody ObraDTO dto) {
        Usuario criador = usuarioService.buscarPorId(dto.usuarioId());
        Obra obra = obraService.criarObra(dto.toEntity(), criador);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ObraDTO(obra));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ObraDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<ObraDTO> obras = obraService.listarObrasDoUsuario(usuarioId)
                .stream().map(ObraDTO::new).toList();
        return ResponseEntity.ok(obras);
    }
}