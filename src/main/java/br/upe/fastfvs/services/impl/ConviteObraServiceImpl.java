package br.upe.fastfvs.services.impl;

import br.upe.fastfvs.entities.ConviteObra;
import br.upe.fastfvs.entities.enums.TipoPermissao;
import br.upe.fastfvs.exceptions.OperacaoInvalidaException;
import br.upe.fastfvs.repositories.ConviteObraRepository;
import br.upe.fastfvs.repositories.MembroObraRepository;
import br.upe.fastfvs.services.ConviteObraService;
import br.upe.fastfvs.services.LinkService;
import br.upe.fastfvs.services.MembroObraService;
import br.upe.fastfvs.services.ObraService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.upe.fastfvs.entities.Obra;

@Service
@RequiredArgsConstructor
public class ConviteObraServiceImpl implements ConviteObraService {

    private final ConviteObraRepository conviteRepository;
    private final ObraService obraService;
    private final MembroObraService membroObraService;
    private final MembroObraRepository membroObraRepository;
    private final LinkService linkService;

    @Override
    public String gerarConvite(Long obraId, TipoPermissao role) {
        Obra obra = obraService.buscarPorId(obraId);
        ConviteObra convite = new ConviteObra(obra, role);
        conviteRepository.save(convite);
        return linkService.gerarLinkConvite(convite.getToken());
    }

    @Override
    @Transactional
    public void aceitarConvite(String token, Long usuarioId) {
        ConviteObra convite = conviteRepository.findByToken(token)
                .orElseThrow(() -> new OperacaoInvalidaException("Convite inválido."));

        if (convite.isExpirado())
            throw new OperacaoInvalidaException("Este convite expirou.");

        // Se já for membro, não adiciona de novo
        boolean jaMembro = membroObraRepository
                .findByUsuarioIdAndObraId(usuarioId, convite.getObra().getId())
                .isPresent();

        if (jaMembro)
            throw new OperacaoInvalidaException("Você já é membro desta obra.");

        membroObraService.adicionarMembro(usuarioId, convite.getObra().getId(), convite.getRole());
    }
}