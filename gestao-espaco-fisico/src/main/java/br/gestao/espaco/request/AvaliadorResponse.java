package br.gestao.espaco.request;

import java.util.Set;

import br.gestao.espaco.model.Perfil;
import br.gestao.espaco.model.Usuario;

public record AvaliadorResponse(Long Id, String Avaliador, Set<Perfil> Perfil) {
	public AvaliadorResponse(Long id, Usuario avaliador) {
		this(id, avaliador.getNome(), avaliador.getPerfis());
	}
}
