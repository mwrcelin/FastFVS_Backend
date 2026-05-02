package br.upe.fastfvs.repositories;

import br.upe.fastfvs.entities.FVS;
import br.upe.fastfvs.entities.HistoricoFVS;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface HistoricoFVSRepository extends JpaRepository<HistoricoFVS, UUID> {
    // Busca o histórico completo de uma ficha específica ordenada por data
    List<HistoricoFVS> findByFvsOrderByDataHoraDesc(FVS fvs);
}