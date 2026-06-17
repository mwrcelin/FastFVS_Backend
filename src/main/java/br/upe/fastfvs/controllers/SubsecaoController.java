package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.ConformidadeResponseDTO;
import br.upe.fastfvs.entities.dtos.EstruturaAutomaticaDTO;
import br.upe.fastfvs.entities.dtos.SubsecaoCreateDTO;
import br.upe.fastfvs.entities.dtos.SubsecaoResponseDTO;
import br.upe.fastfvs.entities.enums.StatusFVS;
import br.upe.fastfvs.services.*;
import jakarta.validation.Valid;
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
    private final ObraService obraService;
    private final QrCodeService qrCodeService;
    private final LinkService linkService;
    private final FVSService fvsService;

    @PostMapping
    public ResponseEntity<SubsecaoResponseDTO> criarSubsecao(
            @RequestBody @Valid SubsecaoCreateDTO dto) {

        Usuario criador = usuarioService.buscarPorId(dto.usuarioId());

        Obra obra = obraService.buscarPorId(dto.obraId());

        Subsecao novaSubsecao = dto.toEntity();
        novaSubsecao.setObra(obra);

        if (dto.paiId() != null) {
            Subsecao pai = subsecaoService.buscarPorId(dto.paiId());
            novaSubsecao.setPai(pai);
        }

        Subsecao salva = subsecaoService.criarSubsecao(novaSubsecao, criador, dto.fvsEscolhidas());

        return ResponseEntity.ok(new SubsecaoResponseDTO(salva));
    }

    //veio de fvs
    @GetMapping("/{subsecaoId}/status-presentes")
    public ResponseEntity<Map<String, Boolean>> statusPresentesNaSubsecao(@PathVariable Long subsecaoId) {
        Map<String, Boolean> resumo = Map.of(
                "NAO_INICIADA", fvsService.existeFvsComStatusNaSubsecao(subsecaoId, StatusFVS.NAO_INICIADA),
                "EM_ANALISE", fvsService.existeFvsComStatusNaSubsecao(subsecaoId, StatusFVS.EM_ANALISE),
                "CONFORME", fvsService.existeFvsComStatusNaSubsecao(subsecaoId, StatusFVS.CONFORME),
                "NAO_CONFORME", fvsService.existeFvsComStatusNaSubsecao(subsecaoId, StatusFVS.NAO_CONFORME)
        );
        return ResponseEntity.ok(resumo);
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


    @GetMapping("/{id}/link")
    public ResponseEntity<Map<String, String>> obterLink(@PathVariable Long id) {
        subsecaoService.buscarPorId(id); // valida existência
        String link = linkService.gerarLinkSubsecao(id);
        return ResponseEntity.ok(Map.of("link", link));
    }

    @GetMapping("/{id}/qrcode")
    public ResponseEntity<Map<String, String>> obterQRCode(@PathVariable Long id) {
        subsecaoService.buscarPorId(id); // valida existência
        String link = linkService.gerarLinkSubsecao(id);
        String qrBase64 = qrCodeService.gerarQRCodeBase64(link, 300, 300);
        return ResponseEntity.ok(Map.of("link", link, "qrcode", qrBase64));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSubsecao(@PathVariable Long id) {
        subsecaoService.deletarSubsecao(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/conformidade")
    public ResponseEntity<ConformidadeResponseDTO> getConformidade(@PathVariable Long id) {
        double valor = subsecaoService.calcularPercentualConformidade(id);
        return ResponseEntity.ok(new ConformidadeResponseDTO(valor));
    }
}