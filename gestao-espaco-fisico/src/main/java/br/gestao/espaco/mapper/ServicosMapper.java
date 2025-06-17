package br.gestao.espaco.mapper;

import org.springframework.stereotype.Service;

import br.gestao.espaco.model.Avaliador;
import br.gestao.espaco.model.Equipamento;
import br.gestao.espaco.model.Feriado;
import br.gestao.espaco.request.AvaliadorResponse;
import br.gestao.espaco.request.EquipamentoRequest;
import br.gestao.espaco.request.EquipamentoResponse;
import br.gestao.espaco.request.FeriadoRequest;
import br.gestao.espaco.request.FeriadoResponse;

@Service
public class ServicosMapper {

	public Feriado toFeriado(FeriadoRequest request) {
		if (request == null) {
			return null;
		}

		return Feriado.builder().nome(request.Nome()).data(request.Data()).build();
	}

	public FeriadoResponse fromFeriado(Feriado feriado) {
		return new FeriadoResponse(feriado.getId(), feriado.getNome(), feriado.getData());
	}

	public Equipamento toEquipamento(EquipamentoRequest request) {
		if (request == null) {
			return null;
		}

		return Equipamento.builder().nome(request.Nome()).build();
	}

	public EquipamentoResponse fromEquipamento(Equipamento equipamento) {
		return new EquipamentoResponse(equipamento.getId(), equipamento.getNome());
	}
	
	public AvaliadorResponse fromAvaliador(Avaliador avaliador) {
		return new AvaliadorResponse(avaliador.getId(), avaliador.getAvaliador());
	}

}
