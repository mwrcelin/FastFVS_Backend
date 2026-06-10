package br.upe.fastfvs.services;

public interface TokenSenhaService {
    void solicitarRecuperacao(String email);
    boolean validarToken(String email, String token);
}