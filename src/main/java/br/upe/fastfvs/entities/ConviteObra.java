package br.upe.fastfvs.entities;

import br.upe.fastfvs.entities.enums.TipoPermissao;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor
public class ConviteObra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne(optional = false)
    @JoinColumn(name = "obra_id")
    private Obra obra;

    @Enumerated(EnumType.STRING)
    private TipoPermissao role; // GERENTE ou PADRAO

    private LocalDateTime dataExpiracao;

    public ConviteObra(Obra obra, TipoPermissao role) {
        this.token = UUID.randomUUID().toString();
        this.obra = obra;
        this.role = role;
        this.dataExpiracao = LocalDateTime.now().plusDays(7);
    }

    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(dataExpiracao);
    }
}