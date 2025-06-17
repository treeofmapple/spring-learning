package br.gestao.espaco.mapper;

import org.springframework.stereotype.Service;

import br.gestao.espaco.model.EspacoFisico;
import br.gestao.espaco.model.Solicitacao;
import br.gestao.espaco.model.Status;
import br.gestao.espaco.model.Usuario;
import br.gestao.espaco.request.SolicitacaoRequest;
import br.gestao.espaco.request.SolicitacaoResponse;

@Service
public class SolicitacaoMapper {

	public Solicitacao toSolicitacoes(SolicitacaoRequest request) {
		if (request == null) {
			return null;
		}

		var espaco = EspacoFisico.builder().id(request.EspacoId()).build();
		var usuario = Usuario.builder().id(request.UsuarioId()).build();

		return Solicitacao.builder().espaco(espaco).solicitante(usuario).nome(request.Nome())
				.dataInicio(request.DataInicio()).dataFim(request.DataFim()).horaInicio(request.HoraInicio())
				.horaFim(request.HoraFim()).status(Status.PENDENTE).justificativa(request.Justificativa()).build();
	}

	public SolicitacaoResponse fromSolicitacoes(Solicitacao sol) {
		return new SolicitacaoResponse(sol.getId(), sol.getEspaco(), sol.getSolicitante(), sol.getNome(),
				sol.getDataInicio(), sol.getDataFim(), sol.getHoraInicio(), sol.getHoraFim(), sol.getDataSolicitacao(),
				sol.getDataStatus(), sol.getStatus(), sol.getJustificativa());
	}

}
