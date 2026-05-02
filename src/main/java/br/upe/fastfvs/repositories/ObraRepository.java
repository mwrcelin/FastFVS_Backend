package br.upe.fastfvs.repositories;

import br.upe.fastfvs.entities.Obra;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ObraRepository extends JpaRepository<Obra, Long> {
    // Traz diretamente a lista de Obras em que o usuário com este ID faz parte
    List<Obra> findByMembrosUsuarioId(Long usuarioId);

    // Busca obras que contenham o texto digitado no nome
    List<Obra> findByNomeContainingIgnoreCase(String nome);
}