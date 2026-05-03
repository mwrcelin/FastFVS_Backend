package br.upe.fastfvs.entities;

import br.upe.fastfvs.entities.enums.StatusFVS;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FVS {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // a criação da FVS offline no celular sem conflitar no banco depois, com log ou int conflitaria

    @Version
    private Long versao; // protege contra alterações simultâneas, pra sincronização offline

    @Column(nullable = false)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusFVS status;

    @Column(nullable = false, updatable = false)
    private Instant dataAbertura;

    @Column
    private Instant dataUltimaEdicao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subsecao_id")
    private Subsecao subsecao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aberta_por_id", updatable = false)
    private Usuario abertaPor;

    @ManyToOne
    @JoinColumn(name = "ultima_edicao_por_id")
    private Usuario ultimaEdicaoPor;


}