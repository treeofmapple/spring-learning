package br.gestao.espaco.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gestao.espaco.model.Feriado;

public interface FeriadoRepository extends JpaRepository<Feriado, Long> {

	boolean existsByNomeAndData(String nome, LocalDate data);

	boolean existsByNome(String nome);

}
