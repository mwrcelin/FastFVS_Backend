package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.*;
import br.upe.fastfvs.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@RequestBody @Valid UsuarioCreateDTO dto) {
        Usuario novoUsuario = usuarioService.cadastrar(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponseDTO(novoUsuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obterPerfil(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
    }

    @PatchMapping("/{id}/dados")
    public ResponseEntity<Void> atualizarDados(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioUpdateDTO dto) {
        usuarioService.atualizarDados(id, dto.nome(), dto.email());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> atualizarSenha(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioSenhaDTO dto) {
        usuarioService.atualizarSenha(id, dto.senhaAtual(), dto.novaSenha());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/foto")
    public ResponseEntity<Void> atualizarFoto(
            @PathVariable Long id,
            @RequestBody UsuarioFotoDTO dto) {
        usuarioService.atualizarFoto(id, dto.fotoUrlOrBase64());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirConta(@PathVariable Long id) {
        usuarioService.excluirConta(id);
        return ResponseEntity.noContent().build();
    }
}