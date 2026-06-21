package br.upe.fastfvs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Habilita a configuração de CORS definida abaixo
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 2. Desabilita proteção CSRF (padrão para APIs REST que usam JWT)
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                    .anyRequest().permitAll() // 👈 troca authenticated() por permitAll()
            );
        // 3. Configura quais rotas são públicas e quais são privadas
            //.authorizeHttpRequests(auth -> auth
               // .requestMatchers("/api/auth/**").permitAll() // Libera geral as rotas de login/cadastro
                //.requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll() // Libera o Swagger
                //.anyRequest().authenticated() // Bloqueia todas as outras rotas

            //);



        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permite requisições de qualquer origem (ideal para desenvolvimento)
        configuration.setAllowedOriginPatterns(List.of("*")); 
        
        // Permite os métodos HTTP que o Flutter vai usar (incluindo OPTIONS que é o "preflight")
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        
        // Permite o envio de qualquer cabeçalho (Headers)
        configuration.setAllowedHeaders(List.of("*"));
        
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}