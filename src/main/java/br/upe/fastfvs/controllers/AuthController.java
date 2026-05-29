package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.LoginRequestDTO;
import br.upe.fastfvs.entities.dtos.UsuarioCreateDTO;
import br.upe.fastfvs.entities.dtos.UsuarioResponseDTO;
import br.upe.fastfvs.services.UsuarioService;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> registrar(@RequestBody @Valid UsuarioCreateDTO dto) {

        // 1. Validação simples de senhas iguais direto no Controller
        if (!dto.senha().equals(dto.confirmarSenha())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("As senhas não correspondem."));
        }

        try {
            try {
                usuarioService.buscarPorEmail(dto.email());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse("Este e-mail já está cadastrado no sistema."));
            } catch (RuntimeException e) {
            }

            Usuario novoUsuario = usuarioService.cadastrar(dto.toEntity());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new UsuarioResponseDTO(novoUsuario));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro interno ao cadastrar usuário."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO request) {
        try {
            Usuario usuario = usuarioService.buscarPorEmail(request.email());

            if (!usuario.getSenha().equals(request.senha())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("E-mail ou senha inválidos."));
            }

            return ResponseEntity.ok(new UsuarioResponseDTO(usuario));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("E-mail ou senha inválidos."));
        }
    }

    public record ErrorResponse(String mensagem) {}
}