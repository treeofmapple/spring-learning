package br.gestao.espaco.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gestao.espaco.model.Equipamento;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

	Optional<Equipamento> findByNome(String nome);

	boolean existsByNome(String nome);

	boolean existsByNomeIn(Set<String> equipamento);

	Set<Equipamento> findByNomeIn(Set<String> equipamento);

}
