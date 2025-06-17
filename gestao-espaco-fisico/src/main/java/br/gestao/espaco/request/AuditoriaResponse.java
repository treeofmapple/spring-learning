package br.gestao.espaco.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import br.gestao.espaco.model.Usuario;
import br.gestao.espaco.request.dto.UsuarioNomeDTO;

public record AuditoriaResponse(Long Id, String Usuario, String Acao, LocalDate Data, LocalTime Hora, String Detalhes) {

	public AuditoriaResponse(Long id, Usuario usuario, String acao, LocalDateTime data, String detalhes) {
		this(id, new UsuarioNomeDTO(usuario).usuario(), acao, data.toLocalDate(), data.toLocalTime(), detalhes);
	}

}