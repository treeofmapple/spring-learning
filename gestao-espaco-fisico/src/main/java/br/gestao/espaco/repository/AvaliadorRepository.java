package br.gestao.espaco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gestao.espaco.model.Avaliador;

@Repository
public interface AvaliadorRepository extends JpaRepository<Avaliador, Long> {

}
