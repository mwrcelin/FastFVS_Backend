package br.upe.fastfvs.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subsecao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne(optional = false)
    @JoinColumn(name = "obra_id")
    private Obra obra;

    @Column(nullable = false)
    private String linkProjeto;
    @ManyToOne

    @JoinColumn(name = "pai_id")
    private Subsecao pai;

    @OneToMany(mappedBy = "pai", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subsecao> filhas;

    @OneToMany(mappedBy = "subsecao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FVS> fichas;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario criador;
}
