package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.EstruturaAutomaticaDTO;
import br.upe.fastfvs.entities.dtos.SubsecaoCreateDTO;
import br.upe.fastfvs.entities.dtos.SubsecaoResponseDTO;
import br.upe.fastfvs.services.SubsecaoService;
import br.upe.fastfvs.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subsecao")
@RequiredArgsConstructor
public class SubsecaoController {

    private final SubsecaoService subsecaoService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<SubsecaoResponseDTO> criarSubsecao(@RequestBody SubsecaoCreateDTO dto) {
        Usuario criador = usuarioService.buscarPorId(dto.usuarioId());

        // 1. Converte para entidade (Nome e Tipo)
        Subsecao novaSubsecao = dto.toEntity();

        // 2. IMPORTANTE: Vincula a Obra (Senão a subseção fica "solta")
        Obra obraRef = new Obra();
        obraRef.setId(dto.obraId());
        novaSubsecao.setObra(obraRef);

        // 3. LOGICA DE HIERARQUIA: Se o front enviar um paiId, vincula aqui
        if (dto.paiId() != null) {
            Subsecao paiRef = new Subsecao();
            paiRef.setId(dto.paiId());
            novaSubsecao.setPai(paiRef);
        }

        // 4. Salva (O Service já trata a criação de FVS automáticas)
        Subsecao salva = subsecaoService.criarSubsecao(novaSubsecao, criador, dto.fvsEscolhidas());

        return ResponseEntity.ok(new SubsecaoResponseDTO(salva));
    }

    @PostMapping("/geracao-automatica")
    public ResponseEntity<Void> criarEstruturaAutomatica(@RequestBody EstruturaAutomaticaDTO dto) {
        // 1. Busca o usuário que está realizando a operação
        Usuario criador = usuarioService.buscarPorId(dto.usuarioId());

        // 2. Chama a lógica pesada que você criou no Service
        subsecaoService.criarEstruturaAutomatica(
                dto.obraId(),
                dto.qtdBlocos(),
                dto.pavPorBloco(),
                dto.aptPorPav(),
                dto.padraoNumeracao(),
                criador,
                dto.fvsEscolhidas()
        );

        // 3. Retorna 200 OK (ou 201 Created) informando que o processo terminou
        return ResponseEntity.ok().build();
    }
}
