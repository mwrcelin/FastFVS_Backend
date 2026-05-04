package br.upe.fastfvs.repositories;

import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubsecaoRepository extends JpaRepository<Subsecao, Long> {
    // Lista as subseções principais de uma obra (onde o pai é null)
    List<Subsecao> findByObraAndPaiIsNull(Obra obra);

    // Lista as subseções filhas de um pavimento, por exemplo
    List<Subsecao> findByPaiId(Long paiId);
}