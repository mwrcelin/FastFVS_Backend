package br.upe.fastfvs.repositories;

import br.upe.fastfvs.entities.Obra;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ObraRepository extends JpaRepository<Obra, Long> {
    List<Obra> findByMembrosUsuarioId(Long usuarioId);

    List<Obra> findByNomeContainingIgnoreCase(String nome);
}