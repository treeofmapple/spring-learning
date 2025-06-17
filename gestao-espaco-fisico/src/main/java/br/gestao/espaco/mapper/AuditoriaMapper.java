package br.gestao.espaco.mapper;

import org.springframework.stereotype.Service;

import br.gestao.espaco.model.Auditoria;
import br.gestao.espaco.model.Usuario;
import br.gestao.espaco.request.AuditoriaRequest;
import br.gestao.espaco.request.AuditoriaResponse;

@Service
public class AuditoriaMapper {

	public Auditoria toAuditoria(AuditoriaRequest request) {
		if (request == null) {
			return null;
		}

		var usuario = Usuario.builder().id(request.UsuarioId()).build();

		return Auditoria.builder().usuario(usuario).acao(request.Acao()).data(request.Data())
				.detalhes(request.Detalhes()).build();
	}

	public AuditoriaResponse fromAuditoria(Auditoria auditoria) {
		return new AuditoriaResponse(auditoria.getId(), auditoria.getUsuario(), auditoria.getAcao(),
				auditoria.getData(), auditoria.getDetalhes());
	}

}
