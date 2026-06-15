package br.upe.fastfvs.services.impl;

import br.upe.fastfvs.entities.TokenResetSenha;
import br.upe.fastfvs.repositories.TokenSenhaRepository;
import br.upe.fastfvs.repositories.UsuarioRepository;
import br.upe.fastfvs.services.TokenSenhaService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class TokenSenhaServiceImpl implements TokenSenhaService {

    private final TokenSenhaRepository tokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final JavaMailSender mailSender;

    @Override
    @Transactional
    public void solicitarRecuperacao(String email) {
        // Verifica se o usuário existe para evitar spam/enumeração, mas não retorna erro.
        if (usuarioRepository.findByEmail(email).isPresent()) {
            String token = String.format("%06d", new Random().nextInt(999999));

            tokenRepository.deleteByEmail(email);
            tokenRepository.save(new TokenResetSenha(email, token));

            enviarEmail(email, token);
        }
    }

    @Override
    public boolean validarToken(String email, String token) {
        return tokenRepository.findByEmailAndToken(email, token)
                .map(t -> !t.isExpirado())
                .orElse(false);
    }

    private void enviarEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Código de Recuperação FastFVS");
        message.setText("Olá! Seu código de recuperação é: " + token + ". Ele expira em 30 minutos.");
        mailSender.send(message);
    }
}