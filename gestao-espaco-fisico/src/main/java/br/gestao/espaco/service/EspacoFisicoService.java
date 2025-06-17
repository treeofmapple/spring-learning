package br.gestao.espaco.service;

import java.beans.Transient;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.gestao.espaco.config.LogAuditoria;
import br.gestao.espaco.exception.AlreadyExistsException;
import br.gestao.espaco.exception.NotFoundException;
import br.gestao.espaco.exception.UnavailableException;
import br.gestao.espaco.mapper.EspacoMapper;
import br.gestao.espaco.model.Disponibilidade;
import br.gestao.espaco.model.EspacoFisico;
import br.gestao.espaco.repository.EquipamentoRepository;
import br.gestao.espaco.repository.EspacoRepository;
import br.gestao.espaco.request.EquipamentoRequest;
import br.gestao.espaco.request.EspacoRequest;
import br.gestao.espaco.request.EspacoResponse;
import br.gestao.espaco.request.dto.DisplayEquipamentoDTO;
import br.gestao.espaco.request.dto.DisponibilidadeDTO;
import br.gestao.espaco.request.dto.EspacoRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EspacoFisicoService {

	private final EspacoRepository espacoRepository;
	private final EquipamentoRepository equipamentoRepository;
	private final EspacoMapper mapper;
	
	@LogAuditoria(acao = "FIND_ALL_ESPACO_FISICO")
	public List<EspacoResponse> findAll() {
		List<EspacoFisico> espaco = espacoRepository.findAll();
		if (espaco.isEmpty()) {
			throw new NotFoundException(String.format("Nenhum espaco fisico foi encontrado"));
		}
		return espaco.stream().map(mapper::fromEspaco).collect(Collectors.toList());
	}

	@LogAuditoria(acao = "FIND_BY_ID_ESPACO_FISICO")
	public EspacoResponse findById(Long espacoId) {
		return espacoRepository.findById(espacoId).map(mapper::fromEspaco).orElseThrow(
				() -> new NotFoundException(String.format("Espaco com id: %s não foi encontrado", espacoId)));
	}

	@LogAuditoria(acao = "DISPLAY_EQUIPAMENTOS_ESPACO_FISICO")
	public DisplayEquipamentoDTO displayEquipamentos(Long id) {
		var espaco = espacoRepository.findById(id).orElseThrow(
				() -> new NotFoundException(String.format("Espaco com id: %s não foi encontrado", id)));
		return new DisplayEquipamentoDTO(espaco.getId(), espaco.getNome(), espaco.getTipo(), espaco.getEquipamentos());
	}

	@LogAuditoria(acao = "VERIFICAR_DISPONIBILIDADE_ESPACO_FISICO")
	public DisponibilidadeDTO verificarDisponibilidade(Long id) {
		var espaco = espacoRepository.findById(id).orElseThrow(
				() -> new NotFoundException(String.format("Espaco com id: %s não foi encontrado", id)));
		return new DisponibilidadeDTO(espaco.getId(), espaco.getNome(), espaco.getTipo(), espaco.getDisponibilidade());
	}

	@Transient
	@LogAuditoria(acao = "CREATE_ESPACO_FISICO")
	public Long createEspaco(EspacoRequest request) {
		if (espacoRepository.existsByNomeAndTipo(request.Nome(), request.Tipo())) {
			throw new AlreadyExistsException(
					String.format("Espaco com o mesmo nome %s e tipo %s ja existe.", request.Nome(), request.Tipo()));
		}

		var espaco = mapper.toEspaco(request);
		espaco.setDisponibilidade(Disponibilidade.DISPONIVEL);
		espacoRepository.save(espaco);
		return espaco.getId();
	}

	@LogAuditoria(acao = "UPDATE_ESPACO_FISICO")
	public void updateEspaco(Long espacoId, EspacoRequestDTO request) {
		var espaco = espacoRepository.findById(espacoId).orElseThrow(() -> new NotFoundException(
				String.format("Não foi possivel atualizar o Espaco, não foi encontrado Espaco com id:: %s", espacoId)));
		mergerEspacoDTO(espaco, request);
		espacoRepository.save(espaco);
	}
	
	@LogAuditoria(acao = "UPDATE_ESPACO_FISICO")
	public void deleteEspaco(Long espacoId) {
		if (!espacoRepository.existsById(espacoId)) {
			throw new NotFoundException("Usuario não encontrado com ID:: %s" + espacoId);
		}
		espacoRepository.deleteById(espacoId);
	}

	@LogAuditoria(acao = "DISPONIVEL_ESPACO_FISICO")
	public void disponivelEspaco(Long espacoId) {
		var espaco = espacoRepository.findById(espacoId).orElseThrow(() -> new NotFoundException(
				String.format("Não foi possivel atualizar o Espaco, não foi encontrado Espaco com id:: %s", espacoId)));
		if (espaco.getDisponibilidade() == Disponibilidade.DISPONIVEL) {
			throw new UnavailableException(
					String.format("O Espaco com id %s, ja está disponivel: ", espacoId));
		}
		espaco.setDisponibilidade(Disponibilidade.DISPONIVEL);
		espacoRepository.save(espaco);
	}

	@LogAuditoria(acao = "INDISPONIVEL_ESPACO_FISICO")
	public void indisponivelEspaco(Long espacoId) {
		var espaco = espacoRepository.findById(espacoId).orElseThrow(() -> new NotFoundException(
				String.format("Não foi possivel atualizar o Espaco, não foi encontrado Espaco com id:: %s", espacoId)));
		if (espaco.getDisponibilidade() == Disponibilidade.INDISPONIVEL) {
			throw new UnavailableException(
					String.format("O Espaco com id %s, ja está disponivel: ", espacoId));
		}
		espaco.setDisponibilidade(Disponibilidade.INDISPONIVEL);
		espacoRepository.save(espaco);
	}

	@LogAuditoria(acao = "INSERIR_EQUIPAMENTO_ESPACO_FISICO")
	public void inserirEquipamento(Long espacoId, EquipamentoRequest request) {
		var equipamento = equipamentoRepository.findByNome(request.Nome())
				.orElseThrow(() -> new NotFoundException(
						String.format("Equipamento com nome %s não foi encontrado", request.Nome())));

		var espaco = espacoRepository.findById(espacoId).orElseThrow(() -> new NotFoundException(
				String.format("Não foi possivel atualizar o Espaco, não foi encontrado Espaco com id:: %s", espacoId)));
		espaco.getEquipamentos().add(equipamento);
		espacoRepository.save(espaco);
	}

	private void mergerEspacoDTO(EspacoFisico espaco, EspacoRequestDTO request) {
		var mapping = mapper.toEspacoDTO(request);
		espaco.setNome(mapping.getNome());
		espaco.setTipo(mapping.getTipo());
		espaco.setMetragem(mapping.getMetragem());
		espaco.setDisponibilidade(mapping.getDisponibilidade());
		espaco.setEquipamentos(mapping.getEquipamentos());
	}

}
