package br.gestao.espaco.request;

import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SolicitacaoRequest(
		
		@NotNull(message = "Id do espaco fisico não pode ser nulo.")
		Long EspacoId,
		
		@NotNull(message = "Id do usuario não pode ser nulo.")
		Long UsuarioId, 
		
		@NotNull(message = "Nome da solicitacao não pode ser nulo")
		@NotBlank(message = "Nome da solitacacao não pode ficar vazio")
		String Nome,
		
		@NotNull(message = "Data do inicio não pode ser nula")
		LocalDate DataInicio, 
		
		@NotNull(message = "Data do fim não pode ser nula")
		@FutureOrPresent
		LocalDate DataFim, 
		
		@NotNull(message = "Hora do fim não pode ser nula")
		@Schema(description = "HH:MM:SS", example = "10:00:00")
		LocalTime HoraInicio,
		
		@NotNull(message = "Hora do fim não pode ser nula")
		@Schema(description = "HH:MM:SS", example = "14:00:00")
		LocalTime HoraFim,
		
		@NotNull(message = "Justificativa não pode ser nula")
		@NotBlank(message = "Justificativa não pode ficar vazia")
		String Justificativa
		
		) {

}
