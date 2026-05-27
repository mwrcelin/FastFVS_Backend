package br.upe.fastfvs.entities;

import br.upe.fastfvs.entities.enums.AcaoFVS;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoFVS {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Celular pode gerar o UUID offline ao registrar uma ação

    @ManyToOne(optional = false)
    @JoinColumn(name = "fvs_id")
    private FVS fvs;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario; // quem fez a alteração (mesmo estando offline)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcaoFVS acao;

    @Column(nullable = false)
    private Instant dataHora; // horario da ação

    @Column
    private String observacao;

}
