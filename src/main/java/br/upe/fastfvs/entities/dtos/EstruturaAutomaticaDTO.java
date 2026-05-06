package br.upe.fastfvs.entities.dtos;

import java.util.List;

public record EstruturaAutomaticaDTO(
        Long obraId,
        int qtdBlocos,
        int pavPorBloco,
        int aptPorPav,
        String padraoNumeracao,
        Long usuarioId,
        List<String> fvsEscolhidas
) {}