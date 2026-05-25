package br.upe.fastfvs.controllers;

import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.entities.dtos.ObraDTO;
import br.upe.fastfvs.services.ObraService;
import br.upe.fastfvs.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.upe.fastfvs.services.QrCodeService;

import java.util.List;

@RestController
@RequestMapping("/api/obras")
@RequiredArgsConstructor
public class ObraController {

    private final ObraService obraService;
    private final UsuarioService usuarioService;
    private final QrCodeService qrCodeService;

    @PostMapping
    public ResponseEntity<ObraDTO> criarObra(@RequestBody ObraDTO dto) {
        Usuario criador = usuarioService.buscarPorId(dto.usuarioId());
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
    public ResponseEntity<String> obterQRCodeObra(@PathVariable Long id) {
        // 1. Busca a obra para garantir que ela existe (se não existir, o service lança exceção)
        Obra obra = obraService.buscarPorId(id);

        // 2. Cria o Deep Link apontando para a tela da Obra
        String urlObra = "https://fastfvs-app.com/obra/" + obra.getId();

        // 3. Gera o QR Code em Base64
        String qrCodeBase64 = qrCodeService.gerarQRCodeBase64(urlObra, 300, 300);

        return ResponseEntity.ok(qrCodeBase64);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObraDTO> buscarPorId(@PathVariable Long id) {
        Obra obra = obraService.buscarPorId(id);
        // O DTO precisa ser instanciado conforme seus construtores disponíveis
        return ResponseEntity.ok(new ObraDTO(obra));
    }

    
}