package br.gestao.espaco.request.dto;

import java.util.Set;

import br.gestao.espaco.model.Equipamento;

public record DisplayEquipamentoDTO(Long Id, String Nome, String Tipo, Set<Equipamento> Equipamentos) {

}
