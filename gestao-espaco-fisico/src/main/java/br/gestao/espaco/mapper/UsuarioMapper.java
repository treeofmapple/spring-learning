package br.gestao.espaco.mapper;

import java.util.Set;

import org.springframework.stereotype.Service;

import br.gestao.espaco.model.Usuario;
import br.gestao.espaco.request.UsuarioRequest;
import br.gestao.espaco.request.UsuarioResponse;

@Service
public class UsuarioMapper {

	public Usuario toUsuario(UsuarioRequest request) {
		if (request == null) {
			return null;
		}

		return Usuario.builder().nome(request.Nome()).email(request.Email()).perfis(request.Perfil() == null ? Set.of() : request.Perfil()).build();
	}

	public UsuarioResponse fromUsuario(Usuario usuario) {
		return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPerfis());
	}

}
