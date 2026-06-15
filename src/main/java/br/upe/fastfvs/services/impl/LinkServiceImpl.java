package br.upe.fastfvs.services.impl;

import br.upe.fastfvs.services.LinkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LinkServiceImpl implements LinkService {

    /**
     * Lido do application.properties:
     *   app.base-url=https://fastfvs-app.com
     *
     * O valor padrão após o ":" é o fallback caso a propriedade não exista.
     */

    @Value("${app.base-url:https://fastfvs-app.com}")
    private String baseUrl;

    @Override
    public String gerarLinkObra(Long obraId) {
        return baseUrl + "/obra/" + obraId;
    }

    @Override
    public String gerarLinkSubsecao(Long subsecaoId) {
        return baseUrl + "/subsecao/" + subsecaoId;
    }

    @Override
    public String gerarLinkConvite(String token) {
        return baseUrl + "/convite/" + token;
    }
}
