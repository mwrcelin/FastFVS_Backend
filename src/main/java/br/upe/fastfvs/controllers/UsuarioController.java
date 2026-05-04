package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.UsuarioResponseDTO;
import br.upe.fastfvs.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/{id}") //
    public ResponseEntity<UsuarioResponseDTO> obterPerfil(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
    }
}