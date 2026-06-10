package br.upe.fastfvs.services;

public interface LinkService {

    /**
     * Gera o link de acesso a uma Obra.
     * Usado para exibição e geração de QR Code.
     */
    String gerarLinkObra(Long obraId);

    /**
     * Gera o link de acesso a uma Subseção.
     * Usado para exibição e geração de QR Code.
     */
    String gerarLinkSubsecao(Long subsecaoId);

    String gerarLinkConvite(String token);
}
