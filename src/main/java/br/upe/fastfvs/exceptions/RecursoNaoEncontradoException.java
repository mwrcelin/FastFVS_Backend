package br.upe.fastfvs.exceptions;

public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Object id) {
        super(recurso + " não encontrado(a) com o ID: " + id);
    }

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
