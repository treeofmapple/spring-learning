package br.gestao.espaco.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.gestao.espaco.config.LogAuditoria;
import br.gestao.espaco.exception.DuplicateException;
import br.gestao.espaco.exception.NotFoundException;
import br.gestao.espaco.mapper.AuditoriaMapper;
import br.gestao.espaco.model.Auditoria;
import br.gestao.espaco.repository.AuditoriaRepository;
import br.gestao.espaco.repository.UsuarioRepository;
import br.gestao.espaco.request.AuditoriaRequest;
import br.gestao.espaco.request.AuditoriaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditoriaService {

	private final UsuarioRepository usuarioRepository;
	private final AuditoriaRepository auditoriaRepository;
	private final AuditoriaMapper mapper;

	public List<AuditoriaResponse> findAll() {
		List<Auditoria> auditoria = auditoriaRepository.findAll();
		if (auditoria.isEmpty()) {
			throw new NotFoundException(String.format("Nenhuma auditoria foi encontrada"));
		}
		return auditoria.stream().map(mapper::fromAuditoria).collect(Collectors.toList());
	}

	public AuditoriaResponse findById(Long auditoriaId) {
		return auditoriaRepository.findById(auditoriaId).map(mapper::fromAuditoria).orElseThrow(
				() -> new NotFoundException(String.format("Auditoria com id: %s não foi encontrado", auditoriaId)));
	}

	public Long createAuditoria(AuditoriaRequest request) {
		var usuarioId = usuarioRepository.findById(request.UsuarioId()).orElseThrow(
				() -> new NotFoundException(String.format("Usuario com id: %s não existe", request.UsuarioId())));

		LocalDateTime windowStart = request.Data().minusSeconds(10);
		LocalDateTime windowEnd = request.Data().plusSeconds(10);

		if (auditoriaRepository.existsByUsuarioAndAcaoAndDataBetween(usuarioId, request.Acao(), windowStart,
				windowEnd)) {
			throw new DuplicateException(String
					.format("Evento de auditoria similar foi detectado em um periodo curto de tempo", request.Data()));
		}
		
		var auditoria = auditoriaRepository.save(mapper.toAuditoria(request));
		return auditoria.getId();
	}

	public void updateAuditoria(Long auditoriaId, AuditoriaRequest request) {
		var auditoria = auditoriaRepository.findById(auditoriaId).orElseThrow(
				() -> new NotFoundException(String.format("Auditoria com id: %s não foi encontrado", auditoriaId)));

		mergerAuditoria(auditoria, request);
		auditoriaRepository.save(auditoria);
	}

	@LogAuditoria(acao = "DELETE_AUDITORIA")
	public void deleteAuditoria(Long auditoriaId) {
		if (!auditoriaRepository.existsById(auditoriaId)) {
			throw new NotFoundException(String.format("Auditoria com id: %s não foi encontrado", auditoriaId));
		}
		auditoriaRepository.deleteById(auditoriaId);
	}

	private void mergerAuditoria(Auditoria auditoria, AuditoriaRequest request) {
		var mapping = mapper.toAuditoria(request);
		auditoria.setUsuario(mapping.getUsuario());
		auditoria.setAcao(mapping.getAcao());
		auditoria.setData(mapping.getData());
		auditoria.setDetalhes(mapping.getDetalhes());
	}

}
