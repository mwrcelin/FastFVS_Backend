package br.upe.fastfvs.exceptions;

public class SenhaIncorretaException extends RuntimeException {

    public SenhaIncorretaException() {
        super("A senha atual informada está incorreta.");
    }
}
