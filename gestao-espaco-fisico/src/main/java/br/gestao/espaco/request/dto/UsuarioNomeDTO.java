package br.gestao.espaco.request.dto;

import br.gestao.espaco.model.Usuario;

public record UsuarioNomeDTO(String usuario) {

    public UsuarioNomeDTO(Usuario usuario) {
        this(usuario != null && usuario.getNome() != null
                ? usuario.getNome()
                : "System");
    }
}
