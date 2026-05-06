package br.upe.fastfvs.repositories;

import br.upe.fastfvs.entities.MembroObra;
import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MembroObraRepository extends JpaRepository<MembroObra, Long> {

    List<MembroObra> findByUsuario(Usuario usuario);
    Optional<MembroObra> findByUsuarioAndObra(Usuario usuario, Obra obra);

    List<MembroObra> findByObraId(Long obraId);

    Optional<MembroObra> findByUsuarioIdAndObraId(Long usuarioId, Long obraId);
}