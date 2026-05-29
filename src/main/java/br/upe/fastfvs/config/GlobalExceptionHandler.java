package br.upe.fastfvs.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            erros.put(fieldName, errorMessage);
        });

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", HttpStatus.BAD_REQUEST.value());
        resposta.put("mensagem", "Validação falhou");
        resposta.put("erros", erros);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {

        if (ex.getMessage() != null && ex.getMessage().contains("não encontrado")) {
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("status", HttpStatus.NOT_FOUND.value());
            resposta.put("mensagem", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        resposta.put("mensagem", "Erro interno no servidor");
        resposta.put("detalhe", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityException(
            DataIntegrityViolationException ex) {

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", HttpStatus.CONFLICT.value());
        resposta.put("mensagem", "Dados inválidos ou duplicados");
        resposta.put("detalhe", "Um ou mais campos violam restrições de banco de dados");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        resposta.put("mensagem", "Erro não esperado");
        resposta.put("tipo", ex.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
    }

}