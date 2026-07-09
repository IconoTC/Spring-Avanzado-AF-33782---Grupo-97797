package com.example;

import java.util.TreeMap;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.EventListener;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

import com.example.domain.event.DomainEvent;
import com.example.domain.event.EntityChangedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
@EnableAspectJAutoProxy
@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Microservicio: Catalogo de peliculas",
                version = "1.0",
                description = "Ejemplo de Microservicio utilizando la base de datos **Sakila**.",
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(name = "Javier Martín", url = "https://github.com/jmagit", email = "support@example.com")
        ),
        externalDocs = @ExternalDocumentation(description = "Documentación del proyecto", url = "https://github.com/jmagit/BOOT20250305/tree/main/catalogo")
)
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@EnableFeignClients
public class CatalogoApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(CatalogoApplication.class, args);
	}

    @Bean
    OpenApiCustomizer sortSchemasAlphabetically() {
        return openApi -> {
            var schemas = openApi.getComponents().getSchemas();
            openApi.getComponents().setSchemas(new TreeMap<>(schemas));
        };
    }
    
    // Application metrics: https://microservices.io/patterns/observability/application-metrics.html
    
//    @Bean
//    HttpExchangeRepository httpTraceRepository() {
//        return new InMemoryHttpExchangeRepository();
//    }
//
//    @Bean
//    AuditEventRepository auditEventRepository() {
//        return new InMemoryAuditEventRepository();
//    }
//    @Bean
//    AuthenticationEventPublisher authenticationEventPublisher
//            (ApplicationEventPublisher applicationEventPublisher) {
//        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
//    }
    
    // Domain events: https://microservices.io/patterns/data/domain-event.html
    
	@EventListener
	void evento(DomainEvent event) {
		log.warn("DomainEvent - ENTITY: %s (%s) KEY: %d OLD: %s NEW: %s".formatted(event.entity(), event.property(),
                event.pk(), event.old(), event.current()));
	}

	// AOT: EntityChangedEventAspect
	
	@EventListener
	void evento(EntityChangedEvent event) {
		log.info("ENTITY: %s (%s) KEY: %d".formatted(event.entity(), event.type(), event.key()));
	}


//	@Autowired
//	KafkaTemplate<String, String> kafkaTemplate;
//	
//	@Bean
//	NewTopic entityChangedTopic() {
//		return TopicBuilder.name("catalogo-events").partitions(1).replicas(1).build();
//	}
//	ObjectMapper converter = new ObjectMapper();
//
//	@EventListener
//	void evento(EntityChangedEvent event) throws JsonProcessingException {
//		kafkaTemplate.send("catalogo-events", event.entity(), converter.writeValueAsString(event))
//			.thenAccept(result -> log.warning("EVENT: %s OFFSET: %s".formatted(event, result.getRecordMetadata().offset())))
//			.exceptionally(ex -> {
//				log.severe("EVENT: %s ERROR: %s".formatted(event, ex.getMessage()));
//				return null;
//			});
//	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		System.err.println("Aplicación arrancada...");
//		publisher.publishEvent("Aplicación arrancada...");
		log.info("Aplicación arrancada...");
	}

}
