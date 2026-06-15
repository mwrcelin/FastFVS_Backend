package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.LoginRequestDTO;
import br.upe.fastfvs.entities.dtos.UsuarioCreateDTO;
import br.upe.fastfvs.entities.dtos.UsuarioResponseDTO;
import br.upe.fastfvs.entities.dtos.ValidacaoDTO;
import br.upe.fastfvs.exceptions.CredenciaisInvalidasException;
import br.upe.fastfvs.exceptions.OperacaoInvalidaException;
import br.upe.fastfvs.exceptions.RecursoNaoEncontradoException;
import br.upe.fastfvs.services.TokenSenhaService;
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
    public ResponseEntity<UsuarioResponseDTO> registrar(@RequestBody @Valid UsuarioCreateDTO dto) {
        if (!dto.senha().equals(dto.confirmarSenha())) {
            throw new OperacaoInvalidaException("As senhas não correspondem.");
        }
        Usuario novoUsuario = usuarioService.cadastrar(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponseDTO(novoUsuario));
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        try {
            Usuario usuario = usuarioService.buscarPorEmail(request.email());
            if (!usuario.getSenha().equals(request.senha())) {
                throw new CredenciaisInvalidasException();
            }
            return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
        } catch (RecursoNaoEncontradoException e) {
            throw new CredenciaisInvalidasException();
        }
    }

    private final TokenSenhaService tokenSenhaService;

    @PostMapping("/solicitar-reset")
    public ResponseEntity<String> solicitar(@RequestParam String email) {
        tokenSenhaService.solicitarRecuperacao(email);
        // Sempre retorna sucesso, mesmo se o e-mail não existir (segurança silênciosa)
        return ResponseEntity.ok("Se o e-mail existir, você receberá o código.");
    }

    // Rota: /api/auth/validar-token
    @PostMapping("/validar-token")
    public ResponseEntity<String> validar(@RequestBody ValidacaoDTO dto) {
        if (tokenSenhaService.validarToken(dto.email(), dto.token())) {
            return ResponseEntity.ok("Token válido.");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou expirado.");
    }
}
