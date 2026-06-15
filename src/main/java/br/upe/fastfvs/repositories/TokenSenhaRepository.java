package br.upe.fastfvs.repositories;

import br.upe.fastfvs.entities.TokenResetSenha;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TokenSenhaRepository extends JpaRepository<TokenResetSenha, Long> {
    Optional<TokenResetSenha> findByEmailAndToken(String email, String token);
    void deleteByEmail(String email);
}