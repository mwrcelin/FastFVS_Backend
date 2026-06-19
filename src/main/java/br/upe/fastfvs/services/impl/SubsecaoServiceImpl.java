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
import br.upe.fastfvs.entities.dtos.NivelHierarquiaDTO;
import br.upe.fastfvs.exceptions.OperacaoInvalidaException;
import java.util.HashMap;
import java.util.Map;

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
            List<NivelHierarquiaDTO> niveis,
            Usuario criador,
            List<String> fvsEscolhidas) {

        Obra obra = obraService.buscarPorId(obraId);

        if (niveis == null || niveis.isEmpty()) {
            throw new OperacaoInvalidaException("É necessário informar ao menos um nível da hierarquia.");
        }

        // contador global de cada nível (chave = nome do nível, valor = próximo número)
        Map<String, Integer> contadores = new HashMap<>();

        gerarNivel(obra, niveis, 0, null, contadores, criador, fvsEscolhidas);
    }

    private void gerarNivel(
            Obra obra,
            List<NivelHierarquiaDTO> niveis,
            int indiceNivel,
            Subsecao pai,
            Map<String, Integer> contadores,
            Usuario criador,
            List<String> fvsEscolhidas) {

        if (indiceNivel >= niveis.size()) {
            return;
        }

        NivelHierarquiaDTO nivelAtual = niveis.get(indiceNivel);

        for (int i = 0; i < nivelAtual.quantidade(); i++) {
            int numero = contadores.merge(nivelAtual.nome(), 1, Integer::sum);

            Subsecao item = new Subsecao();
            item.setNome(nivelAtual.nome() + " " + numero);
            item.setObra(obra);
            item.setPai(pai);

            Subsecao itemSalvo = criarSubsecao(item, criador, fvsEscolhidas);

            // recursão: gera o próximo nível dentro de cada item criado neste nível
            gerarNivel(obra, niveis, indiceNivel + 1, itemSalvo, contadores, criador, fvsEscolhidas);
        }
    }
}