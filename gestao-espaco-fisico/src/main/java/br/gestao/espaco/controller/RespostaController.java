package br.gestao.espaco.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gestao.espaco.request.AuditoriaRequest;
import br.gestao.espaco.request.AuditoriaResponse;
import br.gestao.espaco.request.AvaliadorResponse;
import br.gestao.espaco.request.EquipamentoRequest;
import br.gestao.espaco.request.EspacoRequest;
import br.gestao.espaco.request.EspacoResponse;
import br.gestao.espaco.request.FeriadoRequest;
import br.gestao.espaco.request.SolicitacaoRequest;
import br.gestao.espaco.request.SolicitacaoResponse;
import br.gestao.espaco.request.UsuarioRequest;
import br.gestao.espaco.request.UsuarioResponse;
import br.gestao.espaco.request.dto.DisplayEquipamentoDTO;
import br.gestao.espaco.request.dto.DisponibilidadeDTO;
import br.gestao.espaco.request.dto.SolicitacaoNomeRequestDTO;
import br.gestao.espaco.request.dto.StatusDTO;
import br.gestao.espaco.service.AuditoriaService;
import br.gestao.espaco.service.EspacoFisicoService;
import br.gestao.espaco.service.ServicosGeraisService;
import br.gestao.espaco.service.SolicitacaoService;
import br.gestao.espaco.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/resposta")
@RequiredArgsConstructor
@Tag(name = "Resposta", description = "Operations related to Usuarios")
public class RespostaController {

	private final AuditoriaService a;
	private final EspacoFisicoService b;
	private final ServicosGeraisService c;
	private final SolicitacaoService d;
	private final UsuarioService e;

	@GetMapping("/get/ava")
	public ResponseEntity<List<AvaliadorResponse>> findAllAvaliadores() {
		return ResponseEntity.status(HttpStatus.OK).body(c.findAllAvaliador());
	}

	@PostMapping("/create/usuario")
	public ResponseEntity<Long> createUsuario(@RequestBody @Valid UsuarioRequest request) {
		Long usuarioId = e.createUsuario(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioId);
	}

	@PostMapping("/create/espaco")
	public ResponseEntity<Long> createEspaco(@RequestBody @Valid EspacoRequest request) {
		Long espacoId = b.createEspaco(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(espacoId);
	}

	@PostMapping("/create/equipamento")
	public ResponseEntity<Long> createEquipamento(@RequestBody @Valid EquipamentoRequest request) {
		Long equipamentoId = c.createEquipamento(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(equipamentoId);
	}

	@PostMapping("/create/feriado")
	public ResponseEntity<Long> createFeriado(@RequestBody @Valid FeriadoRequest request) {
		Long feriadoId = c.createFeriadoData(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(feriadoId);
	}

	@PutMapping("/inserirEquipamento/{id}")
	public ResponseEntity<Void> inserirEquipamento(@PathVariable Long id,
			@RequestBody @Valid EquipamentoRequest request) {
		b.inserirEquipamento(id, request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/disponibilidade/{id}")
	public ResponseEntity<DisponibilidadeDTO> verificarDisponibilidade(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(b.verificarDisponibilidade(id));
	}

	@PostMapping("/create/solicitacao")
	public ResponseEntity<Long> createSolicitacao(@RequestBody @Valid SolicitacaoRequest request) {
		Long solicitacaoId = d.createSolicitacao(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(solicitacaoId);
	}

	@PutMapping("/avaliar/{id}{AvaliadorID}")
	public ResponseEntity<SolicitacaoResponse> avaliarSolicitacao(@PathVariable Long id,
			@RequestBody @Valid StatusDTO request, @PathVariable Long AvaliadorID) {
		d.avaliarSolicitacao(id, request, AvaliadorID);
		return ResponseEntity.status(HttpStatus.OK).build();
	} 

	@PutMapping("/cancelar/solicitacao")
	public ResponseEntity<Void> cancelarSolicitacao(@RequestBody @Valid SolicitacaoNomeRequestDTO request) {
		d.cancelarSolicitacao(request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PostMapping("/create/auditoria")
	public ResponseEntity<Long> createAuditoria(@RequestBody @Valid AuditoriaRequest request) {
		Long usuarioId = a.createAuditoria(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioId);
	}

	@GetMapping("/get/auditoria")
	public ResponseEntity<List<AuditoriaResponse>> findAllAuditoria() {
		return ResponseEntity.status(HttpStatus.OK).body(a.findAll());
	}

	@GetMapping("/get/espaco")
	public ResponseEntity<List<EspacoResponse>> findAllEspaco() {
		return ResponseEntity.status(HttpStatus.OK).body(b.findAll());
	}

	@GetMapping("/get/solicitacao")
	public ResponseEntity<List<SolicitacaoResponse>> findAllSolicitacao() {
		return ResponseEntity.status(HttpStatus.OK).body(d.findAll());
	}

	@GetMapping("/aprovadas")
	public ResponseEntity<List<SolicitacaoResponse>> findAprovadas() {
		return ResponseEntity.status(HttpStatus.OK).body(d.findAprovadas());
	}

	@GetMapping("/get/usuarios")
	public ResponseEntity<List<UsuarioResponse>> findAllUsuarios() {
		return ResponseEntity.status(HttpStatus.OK).body(e.findAll());
	}

	@GetMapping("/equipamento/{id}")
	public ResponseEntity<DisplayEquipamentoDTO> listarEquipamentos(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(b.displayEquipamentos(id));
	}

	@PutMapping("/disponivel/{id}")
	public ResponseEntity<Void> disponivelEspaco(@PathVariable Long id) {
		b.disponivelEspaco(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping("/indisponivel/{id}")
	public ResponseEntity<Void> indisponivelEspaco(@PathVariable Long id) {
		b.indisponivelEspaco(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
