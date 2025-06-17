package br.gestao.espaco.request;

import java.util.Set;

import br.gestao.espaco.model.Perfil;

public record UsuarioResponse(Long Id, String Nome, String Email, Set<Perfil> Perfil) {

}
