package br.gestao.espaco.request;

import java.util.Set;

import br.gestao.espaco.model.Disponibilidade;
import br.gestao.espaco.model.Equipamento;
import br.gestao.espaco.request.dto.EquipamentoNomeDTO;

public record EspacoResponse(Long Id, String Nome, String Tipo, double Metragem, Disponibilidade Disponibilidade,
		EquipamentoNomeDTO Equipamentos) {

	public EspacoResponse(Long id, String nome, String tipo, double metragem, Disponibilidade disponibilidade, Set<Equipamento> equipamentos) {
	    this(id, nome, tipo, metragem, disponibilidade, EquipamentoNomeDTO.from(equipamentos));
	}
	
}
