package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.ObraDTO;
import br.upe.fastfvs.services.LinkService;
import br.upe.fastfvs.services.ObraService;
import br.upe.fastfvs.services.QrCodeService;
import br.upe.fastfvs.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/obras")
@RequiredArgsConstructor
public class ObraController {

    private final ObraService obraService;
    private final UsuarioService usuarioService;
    private final QrCodeService qrCodeService;
    private final LinkService linkService;

    @PostMapping
    public ResponseEntity<ObraDTO> criarObra(
            @RequestBody @Valid ObraDTO dto,
            @RequestParam Long usuarioId) {

        Usuario criador = usuarioService.buscarPorId(usuarioId);
        Obra obra = obraService.criarObra(dto.toEntity(), criador);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ObraDTO(obra));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ObraDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<ObraDTO> obras = obraService.listarObrasDoUsuario(usuarioId)
                .stream().map(ObraDTO::new).toList();
        return ResponseEntity.ok(obras);
    }

    @GetMapping("/{id}/qrcode")
    public ResponseEntity<Map<String, String>> obterQRCode(@PathVariable Long id) {
        obraService.buscarPorId(id); // valida existência
        String link = linkService.gerarLinkObra(id);
        String qrBase64 = qrCodeService.gerarQRCodeBase64(link, 300, 300);
        return ResponseEntity.ok(Map.of("link", link, "qrcode", qrBase64));
    }

    @GetMapping("/{id}/link")
    public ResponseEntity<Map<String, String>> obterLink(@PathVariable Long id) {
        obraService.buscarPorId(id); // valida existência
        String link = linkService.gerarLinkObra(id);
        return ResponseEntity.ok(Map.of("link", link));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObraDTO> buscarPorId(@PathVariable Long id) {
        Obra obra = obraService.buscarPorId(id);
        return ResponseEntity.ok(new ObraDTO(obra));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagarObra(@PathVariable Long id) {
        obraService.apagarObra(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/nome")
    public ResponseEntity<ObraDTO> atualizarNome(
            @PathVariable Long id,
            @RequestParam String novoNome) {
        Obra obra = obraService.atualizarNome(id, novoNome);
        return ResponseEntity.ok(new ObraDTO(obra));
    }
}