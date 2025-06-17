package br.gestao.espaco.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.gestao.espaco.config.LogAuditoria;
import br.gestao.espaco.exception.CancelException;
import br.gestao.espaco.exception.DuplicateException;
import br.gestao.espaco.exception.InvalidDateException;
import br.gestao.espaco.exception.NotFoundException;
import br.gestao.espaco.exception.UnavailableException;
import br.gestao.espaco.mapper.SolicitacaoMapper;
import br.gestao.espaco.model.Disponibilidade;
import br.gestao.espaco.model.Solicitacao;
import br.gestao.espaco.model.Status;
import br.gestao.espaco.repository.AvaliadorRepository;
import br.gestao.espaco.repository.EspacoRepository;
import br.gestao.espaco.repository.SolicitacoesRepository;
import br.gestao.espaco.request.SolicitacaoRequest;
import br.gestao.espaco.request.SolicitacaoResponse;
import br.gestao.espaco.request.dto.SolicitacaoNomeRequestDTO;
import br.gestao.espaco.request.dto.StatusDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolicitacaoService {

	private final SolicitacoesRepository solicitacoesRepository;
	private final EspacoRepository espacoRepository;
	private final AvaliadorRepository avaliadorRepository;
	private final SolicitacaoMapper mapper;

	@LogAuditoria(acao = "FIND_ALL_SOLICITACAO")
	public List<SolicitacaoResponse> findAll() {
		List<Solicitacao> solicitacoes = solicitacoesRepository.findAll();
		if (solicitacoes.isEmpty()) {
			throw new NotFoundException(String.format("Nenhuma solicitacao foi encontrada"));
		}
		return solicitacoes.stream().map(mapper::fromSolicitacoes).collect(Collectors.toList());
	}

	@LogAuditoria(acao = "FIND_BY_ID_SOLICITACAO")
	public SolicitacaoResponse findById(Long id) {
		return solicitacoesRepository.findById(id).map(mapper::fromSolicitacoes).orElseThrow(
				() -> new NotFoundException(String.format("Solicitacao com Id:: %s, não foi encontrada", id)));
	}

	@LogAuditoria(acao = "FIND_APROVADAS_SOLICITACAO")
	public List<SolicitacaoResponse> findAprovadas() {
		List<Solicitacao> solicitacoes = solicitacoesRepository.findByStatus(Status.APROVADA);
		if (solicitacoes.isEmpty()) {
			throw new NotFoundException(String.format("Nenhuma solicitacao foi encontrada"));
		}
		return solicitacoes.stream().map(mapper::fromSolicitacoes).collect(Collectors.toList());
	}

	@LogAuditoria(acao = "CREATE_SOLICITACAO")
	public Long createSolicitacao(SolicitacaoRequest request) {
		if (request.DataInicio().isAfter(request.DataFim())) {
			throw new InvalidDateException(String.format("Data de inicio invalida :: %s", request.DataInicio()));
		}

		var espaco = espacoRepository.findById(request.EspacoId()).orElseThrow(
				() -> new NotFoundException(String.format("Espaco com id: %s não foi encontrado", request.EspacoId())));

		if (espacoRepository.existsByNome(request.Nome())) {
			throw new DuplicateException(String.format("O espaco com nome %s, ja existe", request.Nome()));
		}

		if (espaco.getDisponibilidade().equals(Disponibilidade.INDISPONIVEL)) {
			throw new UnavailableException(
					String.format("O espaco solicitado com ID:: %s, esta indisponivel", request.EspacoId()));
		}

		var solicitacao = mapper.toSolicitacoes(request);
		solicitacao.setStatus(Status.PENDENTE);
		solicitacoesRepository.save(solicitacao);
		return solicitacao.getId();
	}

	@LogAuditoria(acao = "UPDATE_SOLICITACAO")
	public void updateSolicitacao(Long id, SolicitacaoRequest request) {
		var solicitacao = solicitacoesRepository.findById(id).orElseThrow(() -> new NotFoundException(String
				.format("Não foi possivel atualizar a Solicitação, não foi encontrado a Solicitacao com id:: %s", id)));
		mergerSolicitacao(solicitacao, request);
		solicitacoesRepository.save(solicitacao);
	}

	@LogAuditoria(acao = "CANCELAR_SOLICITACAO")
	public void cancelarSolicitacao(SolicitacaoNomeRequestDTO request) {
		var solicitacao = solicitacoesRepository.findByNome(request.Nome()).orElseThrow(
				() -> new NotFoundException(String.format("Solicitacao com Nome:: %s, não foi encontrada", request)));
		if (solicitacao.getStatus().equals(Status.PENDENTE)) {
			solicitacoesRepository.delete(solicitacao);
		} else {
			throw new CancelException(
					String.format("Não pode cancelar a solicitacao nesse estado:: %s", solicitacao.getStatus()));
		}
	}

	@LogAuditoria(acao = "AVALIAR_SOLICITACAO")
	public SolicitacaoResponse avaliarSolicitacao(Long id, StatusDTO request, Long ID_Avaliador) {
		var solicitacao = solicitacoesRepository.findById(id).orElseThrow(() -> new NotFoundException(String
				.format("Não foi possivel atualizar a Solicitação, não foi encontrado a Solicitacao com id:: %s", id)));

		var avaliador = avaliadorRepository.findById(ID_Avaliador).orElseThrow(() -> new NotFoundException(String
				.format("Não foi possivel atualizar a Solicitação, não foi encontrado o Avaliador com id:: %s", id)));

		if (solicitacao.getStatus().equals(Status.APROVADA)) {
			var espaco = espacoRepository.findById(solicitacao.getEspaco().getId()).orElseThrow(
					() -> new NotFoundException(String.format("Não foi possível encontrar o Espaço Físico com id:: %s",
							solicitacao.getEspaco().getId())));
			espaco.setDisponibilidade(Disponibilidade.INDISPONIVEL);
			espacoRepository.save(espaco);
		}

		solicitacao.setAvaliador(avaliador);
		solicitacao.setStatus(request.Status());
		solicitacao.setDataStatus(LocalDate.now());
		solicitacoesRepository.save(solicitacao);
		return mapper.fromSolicitacoes(solicitacao);
	}

	@LogAuditoria(acao = "DELETE_SOLICITACAO")
	public void deleteSolicitacao(Long id) {
		if (!solicitacoesRepository.existsById(id)) {
			throw new NotFoundException(String.format("Solicitacao com Id:: %s, não foi encontrada", id));
		}
		solicitacoesRepository.deleteById(id);
	}

	private void mergerSolicitacao(Solicitacao solicitacao, SolicitacaoRequest request) {
		var mapping = mapper.toSolicitacoes(request);
		solicitacao.setEspaco(mapping.getEspaco());
		solicitacao.setSolicitante(mapping.getSolicitante());
		solicitacao.setDataInicio(mapping.getDataInicio());
		solicitacao.setDataFim(mapping.getDataFim());
		solicitacao.setHoraInicio(mapping.getHoraInicio());
		solicitacao.setHoraFim(mapping.getHoraFim());
		solicitacao.setJustificativa(mapping.getJustificativa());
	}

}
