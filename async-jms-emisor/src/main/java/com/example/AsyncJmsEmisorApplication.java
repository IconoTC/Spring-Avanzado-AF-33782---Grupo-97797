package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Microservicio: JMS Emisor", 
				version = "1.0", 
				description = "Ejemplo de emisor JMS con ActiveMQ.", 
				license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html"), 
				contact = @Contact(name = "Javier Martín", url = "https://github.com/jmagit", email = "support@example.com")), 
		externalDocs = @ExternalDocumentation(description = "Documentación del proyecto", url = "https://github.com/jmagit/REM20220725"))
public class AsyncJmsEmisorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsyncJmsEmisorApplication.class, args);
	}
	
	@Bean // Serialize message content to json using TextMessage
	MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

}
