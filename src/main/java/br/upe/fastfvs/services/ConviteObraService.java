package br.upe.fastfvs.services;


import br.upe.fastfvs.entities.enums.TipoPermissao;


public interface ConviteObraService {
    String gerarConvite(Long obraId, TipoPermissao role);
    void aceitarConvite(String token, Long usuarioId);
}