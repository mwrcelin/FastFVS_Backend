package br.upe.fastfvs.repositories;

import br.upe.fastfvs.entities.MembroObra;
import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MembroObraRepository extends JpaRepository<MembroObra, Long> {
    // Lista todos os vínculos de um utilizador específico
    List<MembroObra> findByUsuario(Usuario usuario);

    // Verifica se um utilizador específico faz parte de uma obra
    Optional<MembroObra> findByUsuarioAndObra(Usuario usuario, Obra obra);
}