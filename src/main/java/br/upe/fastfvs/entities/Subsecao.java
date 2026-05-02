package br.upe.fastfvs.entities;

import br.upe.fastfvs.entities.enums.TipoSubsecao;
import jakarta.persistence.*; // Para @Entity, @Id, @GeneratedValue, etc.
import lombok.*;               // Para @Getter, @Setter, etc.
import java.util.List;         // Para a List das obras

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSubsecao tipo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "obra_id")
    private Obra obra;

    @ManyToOne
    @JoinColumn(name = "pai_id")
    private Subsecao pai;

    @OneToMany(mappedBy = "pai", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subsecao> filhas;

    @OneToMany(mappedBy = "subsecao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FVS> fichas;


}
