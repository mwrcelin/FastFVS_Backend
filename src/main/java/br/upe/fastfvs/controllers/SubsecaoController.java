package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.EstruturaAutomaticaDTO;
import br.upe.fastfvs.entities.dtos.SubsecaoCreateDTO;
import br.upe.fastfvs.entities.dtos.SubsecaoResponseDTO;
import br.upe.fastfvs.services.QrCodeService;
import br.upe.fastfvs.services.SubsecaoService;
import br.upe.fastfvs.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subsecao")
@RequiredArgsConstructor
public class SubsecaoController {

    private final SubsecaoService subsecaoService;
    private final UsuarioService usuarioService;
    private final QrCodeService qrCodeService;

    @PostMapping
    public ResponseEntity<SubsecaoResponseDTO> criarSubsecao(@RequestBody SubsecaoCreateDTO dto) {
        Usuario criador = usuarioService.buscarPorId(dto.usuarioId());

        Subsecao novaSubsecao = dto.toEntity();

        Obra obraRef = new Obra();
        obraRef.setId(dto.obraId());
        novaSubsecao.setObra(obraRef);

        if (dto.paiId() != null) {
            Subsecao paiRef = new Subsecao();
            paiRef.setId(dto.paiId());
            novaSubsecao.setPai(paiRef);
        }

        Subsecao salva = subsecaoService.criarSubsecao(novaSubsecao, criador, dto.fvsEscolhidas());

        return ResponseEntity.ok(new SubsecaoResponseDTO(salva));
    }

    @PostMapping("/geracao-automatica")
    public ResponseEntity<Void> criarEstruturaAutomatica(@RequestBody EstruturaAutomaticaDTO dto) {
        Usuario criador = usuarioService.buscarPorId(dto.usuarioId());

        subsecaoService.criarEstruturaAutomatica(
                dto.obraId(),
                dto.qtdBlocos(),
                dto.pavPorBloco(),
                dto.aptPorPav(),
                dto.padraoNumeracao(),
                criador,
                dto.fvsEscolhidas()
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/qrcode")
    public ResponseEntity<String> obterQRCodeSubsecao(@PathVariable Long id) {
        Subsecao subsecao = subsecaoService.buscarPorId(id); // <- Adicione esse método no seu Service!

        // 2. Cria o Deep Link apontando para a tela da Subseção
        String urlSubsecao = "https://fastfvs-app.com/subsecao/" + id;

        // 3. Gera o QR Code
        String qrCodeBase64 = qrCodeService.gerarQRCodeBase64(urlSubsecao, 300, 300);

        return ResponseEntity.ok(qrCodeBase64);
    }

    @GetMapping("/{paiId}/filhas")
    public ResponseEntity<List<SubsecaoResponseDTO>> listarFilhas(@PathVariable Long paiId) {
        List<SubsecaoResponseDTO> lista = subsecaoService.listarFilhas(paiId)
                .stream()
                .map(SubsecaoResponseDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubsecaoResponseDTO> buscarPorId(@PathVariable Long id) {
        Subsecao subsecao = subsecaoService.buscarPorId(id);
        return ResponseEntity.ok(new SubsecaoResponseDTO(subsecao));
    }

    @GetMapping("/obra/{obraId}/raizes")
    public ResponseEntity<List<SubsecaoResponseDTO>> listarRaizesPorObra(@PathVariable Long obraId) {
        List<SubsecaoResponseDTO> lista = subsecaoService.listarRaizPorObra(obraId)
                .stream()
                .map(SubsecaoResponseDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}/caminho")
    public ResponseEntity<Map<String, String>> obterCaminhoCompleto(@PathVariable Long id) {
        String caminho = subsecaoService.obterCaminhoCompleto(id);
        return ResponseEntity.ok(Map.of("caminho", caminho));
    }
}

