package br.upe.fastfvs.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WellKnownController {

    @GetMapping("/.well-known/assetlinks.json")
    public ResponseEntity<String> assetLinks() {
        String json = """
            [{
              "relation": ["delegate_permission/common.handle_all_urls"],
              "target": {
                "namespace": "android_app",
                "package_name": "br.upe.fastfvs",
                "sha256_cert_fingerprints": ["EC:8F:A4:3C:F2:9E:FC:10:75:0B:55:8B:1E:23:CF:C6:7A:CF:F9:12:D7:32:C6:48:51:F5:29:95:0D:0D:70:C9"]
              }
            }]
            """;

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }
}