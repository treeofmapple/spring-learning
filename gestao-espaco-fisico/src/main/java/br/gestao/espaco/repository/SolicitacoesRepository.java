package br.gestao.espaco.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gestao.espaco.model.Solicitacao;
import br.gestao.espaco.model.Status;

@Repository
public interface SolicitacoesRepository extends JpaRepository<Solicitacao, Long> {

	List<Solicitacao> findByStatus(Status aprovada);

	Optional<Solicitacao> findByNome(String nome);

}
