package br.upe.fastfvs.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "token_reset_senha")
@Getter @Setter @NoArgsConstructor
public class TokenResetSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String token;
    private LocalDateTime dataExpiracao;

    public TokenResetSenha(String email, String token) {
        this.email = email;
        this.token = token;
        this.dataExpiracao = LocalDateTime.now().plusMinutes(30);
    }

    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(dataExpiracao);
    }
}