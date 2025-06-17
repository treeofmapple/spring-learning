package br.gestao.espaco.request.dto;

import java.util.Set;
import java.util.stream.Collectors;

import br.gestao.espaco.model.Equipamento;

public record EquipamentoNomeDTO(Set<String> Nome) {
	public static EquipamentoNomeDTO from(Set<Equipamento> equipamentos) {
		Set<String> nomes = equipamentos.stream().map(Equipamento::getNome).collect(Collectors.toSet());
		return new EquipamentoNomeDTO(nomes);
	}
}