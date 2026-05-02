package br.upe.fastfvs.repositories;

import br.upe.fastfvs.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Busca por email para autenticação
    Optional<Usuario> findByEmail(String email);
}