package br.upe.fastfvs.exceptions;

public class PermissaoNegadaException extends RuntimeException {

    public PermissaoNegadaException() {
        super("Você não tem permissão para realizar esta operação.");
    }

    public PermissaoNegadaException(String mensagem) {
        super(mensagem);
    }
}
