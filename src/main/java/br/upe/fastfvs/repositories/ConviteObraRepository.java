package br.upe.fastfvs.repositories;

import br.upe.fastfvs.entities.ConviteObra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConviteObraRepository extends JpaRepository<ConviteObra, Long> {
    Optional<ConviteObra> findByToken(String token);
}