package br.gestao.espaco.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gestao.espaco.model.Auditoria;
import br.gestao.espaco.model.Usuario;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

	boolean existsByUsuarioAndAcaoAndDataBetween(Usuario usuario, String acao, LocalDateTime windowStart, LocalDateTime windowEnd);

}
