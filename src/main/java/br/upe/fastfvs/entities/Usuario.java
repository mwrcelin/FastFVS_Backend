package br.upe.fastfvs.entities;

import jakarta.persistence.*; // Para @Entity, @Id, @GeneratedValue, etc.
import lombok.*;               // Para @Getter, @Setter, etc.
import java.util.List;         // Para a List das obras

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    private String senha;

    private String fotoPerfil;

    @OneToMany(mappedBy = "usuario")
    private List<MembroObra> obras;
}