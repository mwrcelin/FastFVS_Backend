package br.upe.fastfvs.services;

import br.upe.fastfvs.entities.MembroObra;
import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.enums.TipoPermissao;
import java.util.List;

public interface MembroObraService {


    MembroObra adicionarMembro(Obra obra, Usuario usuario, TipoPermissao role);

    MembroObra adicionarMembro(Long usuarioId, Long obraId, TipoPermissao role);

    List<MembroObra> listarMembrosPorObra(Long obraId);
    TipoPermissao buscarPermissao(Long usuarioId, Long obraId);

    void removerMembro(Long id);
    MembroObra alterarRole(Long id, String novaRole);
}