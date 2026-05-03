package br.upe.fastfvs.repositories;

import br.upe.fastfvs.entities.FVS;
import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.enums.StatusFVS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FVSRepository extends JpaRepository<FVS, UUID> {
    // Lista todas as fichas de uma subseção (ex: Apt 101)
    List<FVS> findBySubsecao(Subsecao subsecao);

    // NOVO: Para o filtro de navegação (Ex: Quero ver só as FVS "NÃO CONFORMES" do Apt 101)
    List<FVS> findBySubsecaoAndStatus(Subsecao subsecao, StatusFVS status);

    // NOVO: Para o Resumo de Conformidade. Conta quantas FVS existem com um certo status na OBRA inteira
    @Query("SELECT COUNT(f) FROM FVS f WHERE f.subsecao.obra.id = :obraId AND f.status = :status")
    long countByObraIdAndStatus(@Param("obraId") Long obraId, @Param("status") StatusFVS status);

    @Query("SELECT COUNT(f) FROM FVS f WHERE f.subsecao.obra.id = :obraId")
    long countBySubsecaoObraId(@Param("obraId") Long obraId);
}
