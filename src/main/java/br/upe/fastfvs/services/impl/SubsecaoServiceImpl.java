package br.upe.fastfvs.services.impl;

import br.upe.fastfvs.entities.FVS;
import br.upe.fastfvs.entities.Obra;
import br.upe.fastfvs.entities.Subsecao;
import br.upe.fastfvs.entities.Usuario;
import br.upe.fastfvs.exceptions.RecursoNaoEncontradoException;
import br.upe.fastfvs.repositories.SubsecaoRepository;
import br.upe.fastfvs.services.FVSService;
import br.upe.fastfvs.services.LinkService;
import br.upe.fastfvs.services.ObraService;
import br.upe.fastfvs.services.SubsecaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubsecaoServiceImpl implements SubsecaoService {

    private final SubsecaoRepository repository;
    private final FVSService fvsService;
    private final ObraService obraService;
    private final LinkService linkService;

    @Override
    @Transactional
    public Subsecao criarSubsecao(Subsecao subsecao, Usuario criador, List<String> fvsEscolhidas) {

        subsecao.setCriador(criador);

        subsecao.setLinkProjeto("");
        Subsecao salva = repository.save(subsecao);

        salva.setLinkProjeto(linkService.gerarLinkSubsecao(salva.getId()));
        salva = repository.save(salva);

        if (fvsEscolhidas != null && !fvsEscolhidas.isEmpty()) {
            for (String titulo : fvsEscolhidas) {
                FVS novaFvs = new FVS();
                novaFvs.setTitulo(titulo);
                novaFvs.setSubsecao(salva);
                fvsService.criarFVS(novaFvs, criador);
            }
        }

        return salva;
    }

    @Override
    public List<Subsecao> listarRaizPorObra(Long obraId) {
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
                .orElseThrow(() -> new RecursoNaoEncontradoException("Subseção", id));
    }

    @Override
    public String obterCaminhoCompleto(Long subsecaoId) {
        Subsecao subsecao = buscarPorId(subsecaoId);

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
    public void deletarSubsecao(Long id) {
        Subsecao subsecao = buscarPorId(id);
        repository.delete(subsecao);
    }

    @Override
    public double calcularPercentualConformidade(Long subsecaoId) {
        buscarPorId(subsecaoId);
        return fvsService.calcularPercentualConformidadePorSubsecao(subsecaoId);
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

        Obra obra = obraService.buscarPorId(obraId);

        int numeroBaseApto = 0;
        try {
            if (padraoNumeracao != null && padraoNumeracao.contains("-")) {
                String parteInicial = padraoNumeracao.split("-")[0].trim();
                numeroBaseApto = Integer.parseInt(parteInicial);
            }
        } catch (NumberFormatException e) {
            numeroBaseApto = 0;
        }

        int contadorPavimento = 1;
        int contadorApto = numeroBaseApto + 1;

        for (int b = 1; b <= qtdBlocos; b++) {
            Subsecao bloco = new Subsecao();
            bloco.setNome("Bloco " + b);
            bloco.setObra(obra);
            bloco.setPai(null);

            Subsecao blocoSalvo = criarSubsecao(bloco, criador, fvsEscolhidas);

            for (int p = 1; p <= pavPorBloco; p++) {
                Subsecao pavimento = new Subsecao();
                pavimento.setNome("Pavimento " + contadorPavimento);
                pavimento.setObra(obra);
                pavimento.setPai(blocoSalvo);
                Subsecao pavSalvo = criarSubsecao(pavimento, criador, fvsEscolhidas);

                contadorPavimento++;

                for (int a = 1; a <= aptPorPav; a++) {
                    Subsecao apartamento = new Subsecao();
                    apartamento.setNome("Apartamento " + contadorApto);
                    apartamento.setObra(obra);
                    apartamento.setPai(pavSalvo);

                    criarSubsecao(apartamento, criador, fvsEscolhidas);
                    contadorApto++;
                }
            }
        }
    }
}