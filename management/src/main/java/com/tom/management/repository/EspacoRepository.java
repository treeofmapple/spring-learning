package br.gestao.espaco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gestao.espaco.model.EspacoFisico;

@Repository
public interface EspacoRepository extends JpaRepository<EspacoFisico, Long> {

	boolean existsByNomeAndTipo(String nome, String tipo);

	boolean existsByNome(String nome);

}
