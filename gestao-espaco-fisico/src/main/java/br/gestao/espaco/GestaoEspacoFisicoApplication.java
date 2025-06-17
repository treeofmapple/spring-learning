package br.gestao.espaco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GestaoEspacoFisicoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestaoEspacoFisicoApplication.class, args);
	}
	
}
