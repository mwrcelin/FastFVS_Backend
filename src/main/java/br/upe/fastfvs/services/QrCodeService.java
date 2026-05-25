package br.upe.fastfvs.services;

public interface QrCodeService {
    String gerarQRCodeBase64(String texto, int largura, int altura);
}