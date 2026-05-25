package br.upe.fastfvs.services.impl;

import br.upe.fastfvs.entities.FVS;
import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.repositories.SubsecaoRepository;
import br.upe.fastfvs.services.FVSService;
import br.upe.fastfvs.services.SubsecaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubsecaoServiceImpl implements SubsecaoService {

    private final SubsecaoRepository repository;
    private final FVSService fvsService; // Injetado para criar as FVS com histórico


    @Override
    @Transactional
    public Subsecao criarSubsecao(Subsecao subsecao, Usuario criador, List<String> fvsEscolhidas) {
        // 1. Salva a subseção primeiro para gerar o ID
        Subsecao salva = repository.save(subsecao);

        // 2. Cria as FVS automaticamente baseadas nos títulos escolhidos (ex: "Alvenaria", "Pintura")
        if (fvsEscolhidas != null && !fvsEscolhidas.isEmpty()) {
            for (String titulo : fvsEscolhidas) {
                FVS novaFvs = new FVS();
                novaFvs.setTitulo(titulo);
                novaFvs.setSubsecao(salva);
                // O fvsService já trata datas e status inicial
                fvsService.criarFVS(novaFvs, criador);
            }
        }
        return salva;
    }

    @Override
    public List<Subsecao> listarRaizPorObra(Long obraId) {
        // referência da obra apenas com o ID para a consulta
        Obra obra = new Obra();
        obra.setId(obraId);
        return repository.findByObraAndPaiIsNull(obra);
    }

    @Override
    public List<Subsecao> listarFilhas(Long paiId) {
        return repository.findByPaiId(paiId);
    }


    @Override
    public Subsecao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subseção não encontrada com o ID: " + id));
    }

    @Override
    public String obterCaminhoCompleto(Long subsecaoId) {
        Subsecao subsecao = repository.findById(subsecaoId)
                .orElseThrow(() -> new RuntimeException("Subseção não encontrada"));

        StringBuilder caminho = new StringBuilder(subsecao.getNome());
        Subsecao pai = subsecao.getPai();

        while (pai != null) {
            caminho.insert(0, pai.getNome() + " > ");
            pai = pai.getPai();
        }

        return caminho.toString();
    }

    @Override
    @Transactional
    public void criarEstruturaAutomatica(
            Long obraId,
            int qtdBlocos,
            int pavPorBloco,
            int aptPorPav,
            String padraoNumeracao,
            Usuario criador,
            List<String> fvsEscolhidas) {

        // 1. Recupera a obra
        Obra obra = new Obra();
        obra.setId(obraId);

        // 2. Lógica para extrair o início da numeração do padrão (ex: "100 - 200" -> 100)
        int numeroBaseApto = 0;
        try {
            if (padraoNumeracao != null && padraoNumeracao.contains("-")) {
                String parteInicial = padraoNumeracao.split("-")[0].trim();
                numeroBaseApto = Integer.parseInt(parteInicial);
            }
        } catch (NumberFormatException e) {
            numeroBaseApto = 0; // Fallback caso o formato esteja errado
        }

        int contadorPavimento = 1;
        int contadorApto = numeroBaseApto + 1;

        // 3. Loop de Blocos
        for (int b = 1; b <= qtdBlocos; b++) {
            Subsecao bloco = new Subsecao();
            bloco.setNome("Bloco " + b);
            bloco.setObra(obra);
            bloco.setPai(null); // Bloco é raiz
            Subsecao blocoSalvo = repository.save(bloco);

            // 4. Loop de Pavimentos por Bloco
            for (int p = 1; p <= pavPorBloco; p++) {
                Subsecao pavimento = new Subsecao();
                pavimento.setNome("Pavimento " + contadorPavimento);
                pavimento.setObra(obra);
                pavimento.setPai(blocoSalvo);
                Subsecao pavSalvo = repository.save(pavimento);

                contadorPavimento++; // Incrementa globalmente conforme seu exemplo

                // 5. Loop de Apartamentos por Pavimento
                for (int a = 1; a <= aptPorPav; a++) {
                    Subsecao apartamento = new Subsecao();
                    apartamento.setNome("Apartamento " + contadorApto);
                    apartamento.setObra(obra);
                    apartamento.setPai(pavSalvo);

                    // Chamamos o método existente para salvar e já criar as FVS vinculadas
                    this.criarSubsecao(apartamento, criador, fvsEscolhidas);

                    contadorApto++; // Incrementa globalmente conforme seu exemplo
                }
            }
        }


    }
}