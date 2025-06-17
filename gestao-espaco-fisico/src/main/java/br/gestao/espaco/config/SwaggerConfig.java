package br.gestao.espaco.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(
	    info = @Info(
	        title = "Gestão de Espaço Fisico",
	        description = "Microsservico de gestão de espaço fisico",
	        version = "1.0A",
	        contact = @Contact(
	            name = "Pogeku",
	            url = "https://"
	        )
	    ),
	    servers = {
	        @Server(url = "http://localhost:8000", description = "Servidor de desenvolvimento")
	    }
	)
public class SwaggerConfig {
	
    @Bean
    public GroupedOpenApi usuarioApi() {
        return GroupedOpenApi.builder()
                .group("Resposta-Controller")
                .pathsToMatch("/resposta/**") 
                .build();
    }
    
    @Bean
    public GroupedOpenApi allControllersApi() {
        return GroupedOpenApi.builder()
                .group("All Controllers")
                .pathsToMatch("/**") 
                .pathsToExclude("/resposta/**")
                .build();
    }
}
