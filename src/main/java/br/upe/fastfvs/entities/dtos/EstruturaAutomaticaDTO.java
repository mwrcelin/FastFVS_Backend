package br.upe.fastfvs.entities.dtos;

import java.util.List;

public record EstruturaAutomaticaDTO(
        Long obraId,
        List<NivelHierarquiaDTO> niveis,
        Long usuarioId,
        List<String> fvsEscolhidas

) {}