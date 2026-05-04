package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.UsuarioCreateDTO;
import br.upe.fastfvs.entities.dtos.UsuarioResponseDTO;
import br.upe.fastfvs.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> registrar(@RequestBody UsuarioCreateDTO dto) {
        Usuario usuario = usuarioService.cadastrar(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponseDTO(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(@RequestBody UsuarioCreateDTO loginDTO) {
        // (autenticação real tem que usar o security)
        Usuario usuario = usuarioService.buscarPorEmail(loginDTO.email());
        if (usuario.getSenha().equals(loginDTO.senha())) {
            return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}