package br.gestao.espaco.config;

import java.time.LocalDateTime;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import br.gestao.espaco.model.Auditoria;
import br.gestao.espaco.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditoriaConfig {

	private final AuditoriaRepository auditoriaRepository;

	@Pointcut("@annotation(logAuditoria)")
	public void logAuditoriaPointcut(LogAuditoria logAuditoria) {
	}

	@AfterReturning(value = "logAuditoriaPointcut(logAuditoria)", argNames = "logAuditoria")
	public void salvarAuditoria(LogAuditoria logAuditoria) {
		var auditoria = Auditoria.builder().acao(logAuditoria.acao()).data(LocalDateTime.now())
				.detalhes("Ação realizada automaticamente pelo aspecto.").build();

		auditoriaRepository.save(auditoria);
	}
}