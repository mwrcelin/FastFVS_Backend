package br.upe.fastfvs.repositories;

import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubsecaoRepository extends JpaRepository<Subsecao, Long> {

    List<Subsecao> findByObraAndPaiIsNull(Obra obra);

    List<Subsecao> findByPaiId(Long paiId);
}