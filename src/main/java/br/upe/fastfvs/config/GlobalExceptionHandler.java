package br.upe.fastfvs.config;

import br.upe.fastfvs.exceptions.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 — campos inválidos (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = ((FieldError) error).getField();
            erros.put(campo, error.getDefaultMessage());
        });
        return build(HttpStatus.BAD_REQUEST, "Validação falhou", erros);
    }

    // 400 — regra de negócio inválida
    @ExceptionHandler(OperacaoInvalidaException.class)
    public ResponseEntity<Map<String, Object>> handleOperacaoInvalida(OperacaoInvalidaException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    // 401 — credenciais de login inválidas
    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<Map<String, Object>> handleCredenciaisInvalidas(CredenciaisInvalidasException ex) {
        return build(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
    }

    // 401 — senha atual errada
    @ExceptionHandler(SenhaIncorretaException.class)
    public ResponseEntity<Map<String, Object>> handleSenhaIncorreta(SenhaIncorretaException ex) {
        return build(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
    }

    // 403 — sem permissão
    @ExceptionHandler(PermissaoNegadaException.class)
    public ResponseEntity<Map<String, Object>> handlePermissaoNegada(PermissaoNegadaException ex) {
        return build(HttpStatus.FORBIDDEN, ex.getMessage(), null);
    }

    // 404 — recurso não encontrado
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleNaoEncontrado(RecursoNaoEncontradoException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    // 409 — e-mail duplicado
    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<Map<String, Object>> handleEmailDuplicado(EmailJaCadastradoException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    // 409 — violação de constraint no banco
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleIntegridade(DataIntegrityViolationException ex) {
        return build(HttpStatus.CONFLICT, "Dados inválidos ou duplicados",
                "Um ou mais campos violam restrições de banco de dados");
    }

    // 500 — qualquer RuntimeException não mapeada
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor", ex.getMessage());
    }

    // 500 — fallback genérico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro não esperado", ex.getClass().getSimpleName());
    }

    // -------------------------------------------------------------------------
    // Auxiliar
    // -------------------------------------------------------------------------

    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String mensagem, Object detalhe) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("mensagem", mensagem);
        body.put("timestamp", Instant.now().toString());
        if (detalhe != null) {
            body.put("detalhe", detalhe);
        }
        return ResponseEntity.status(status).body(body);
    }
}
