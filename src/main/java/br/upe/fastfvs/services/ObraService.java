package br.upe.fastfvs.services;

import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.Usuario;
import java.util.List;

public interface ObraService {
    Obra criarObra(Obra obra, Usuario criador);
    List<Obra> listarObrasDoUsuario(Long usuarioId);
}