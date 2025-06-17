package br.gestao.espaco.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.gestao.espaco.config.LogAuditoria;
import br.gestao.espaco.exception.AlreadyExistsException;
import br.gestao.espaco.exception.DuplicateException;
import br.gestao.espaco.exception.NotFoundException;
import br.gestao.espaco.mapper.ServicosMapper;
import br.gestao.espaco.model.Avaliador;
import br.gestao.espaco.model.Equipamento;
import br.gestao.espaco.model.Feriado;
import br.gestao.espaco.repository.AvaliadorRepository;
import br.gestao.espaco.repository.EquipamentoRepository;
import br.gestao.espaco.repository.FeriadoRepository;
import br.gestao.espaco.request.AvaliadorResponse;
import br.gestao.espaco.request.EquipamentoRequest;
import br.gestao.espaco.request.EquipamentoResponse;
import br.gestao.espaco.request.FeriadoRequest;
import br.gestao.espaco.request.FeriadoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServicosGeraisService {

	public final EquipamentoRepository equipamentoRepository;
	public final FeriadoRepository feriadoRepository;
	public final AvaliadorRepository avaliadorRepository;
	public final ServicosMapper mapper;

	@LogAuditoria(acao = "FIND_ALL_AVALIADORES")
	public List<AvaliadorResponse> findAllAvaliador() {
		List<Avaliador> avaliador = avaliadorRepository.findAll();
		if (avaliador.isEmpty()) {
			throw new NotFoundException(String.format("Nenhum avaliador foi encontrado"));
		}
		return avaliador.stream().map(mapper::fromAvaliador).collect(Collectors.toList());
	}

	@LogAuditoria(acao = "FIND_ALL_FERIADOS")
	public List<FeriadoResponse> findAllFeriados() {
		List<Feriado> feriado = feriadoRepository.findAll();
		if (feriado.isEmpty()) {
			throw new NotFoundException(String.format("Nenhum feriado foi encontrado"));
		}
		return feriado.stream().map(mapper::fromFeriado).collect(Collectors.toList());
	}

	@LogAuditoria(acao = "FIND_ALL_EQUIPAMENTOS")
	public List<EquipamentoResponse> findAllEquipamentos() {
		List<Equipamento> equipamento = equipamentoRepository.findAll();
		if (equipamento.isEmpty()) {
			throw new NotFoundException(String.format("Nenhum equipamento foi encontrado."));
		}
		return equipamento.stream().map(mapper::fromEquipamento).collect(Collectors.toList());
	}

	@LogAuditoria(acao = "CREATE_FERIADO_DATA")
	public Long createFeriadoData(FeriadoRequest request) {
		if (feriadoRepository.existsByNomeAndData(request.Nome(), request.Data())) {
			throw new DuplicateException(String.format("Feriado com nome e data semelhante ja existe:: %s, %s.",
					request.Nome(), request.Data()));
		}

		if (feriadoRepository.existsByNome(request.Nome())) {
			throw new DuplicateException(String.format("Feriado com nome semelhante:: %s", request.Nome()));
		}

		var feriado = feriadoRepository.save(mapper.toFeriado(request));
		return feriado.getId();
	}

	@LogAuditoria(acao = "CREATE_EQUIPAMENTO")
	public Long createEquipamento(EquipamentoRequest request) {
		if (equipamentoRepository.existsByNome(request.Nome())) {
			throw new AlreadyExistsException(
					String.format("Equipamento com o mesmo nome ja existe:: %s.", request.Nome()));
		}
		var equipamento = equipamentoRepository.save(mapper.toEquipamento(request));
		return equipamento.getId();
	}

	@LogAuditoria(acao = "DELETE_EQUIPAMENTO")
	public void deleteEquipamento(Long id) {
		if (!equipamentoRepository.existsById(id)) {
			throw new NotFoundException(String.format("Equipamento com ID:: %s, não foi encontrado.", id));
		}
		equipamentoRepository.deleteById(id);
	}

	@LogAuditoria(acao = "DELETE_FERIADO")
	public void deleteFeriado(Long id) {
		if (!feriadoRepository.existsById(id)) {
			throw new NotFoundException(String.format("Feriado com ID:: %s, não foi encontrado.", id));
		}
		feriadoRepository.deleteById(id);
	}

}
