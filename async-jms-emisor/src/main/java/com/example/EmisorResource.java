package com.example;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.datafaker.Faker;

@RestController
public class EmisorResource {
	@Value("${spring.application.name}:${server.port}")
	private String origen;

	@Autowired
	private JmsTemplate jms;
	
	private Faker faker = new Faker(Locale.of("es", "ES"));
	
	@Tag(name = "Queues")
	@GetMapping(path = "/queues/saludo/{nombre}")
	public String saluda(@PathVariable String nombre) {
		String msg = "Hola " + nombre;
		jms.setPubSubDomain(false);
		jms.convertAndSend("saludos", new MessageDTO(msg, origen));
		return "SEND COLA: " + msg;
	}

	@Tag(name = "Queues")
	@GetMapping(path = "/queues/rafaga")
	public String saluda(@RequestParam(defaultValue = "5") int cantidad) {
		jms.setPubSubDomain(false);
		if(cantidad <= 0) cantidad = 1;
		for(int i=0; i < cantidad; i++)
			jms.convertAndSend("saludos", new MessageDTO("Hola " + faker.name().fullName(), origen));
		return "SEND COLA: %d mensajes".formatted(cantidad);
	}
	
	@Tag(name = "Topics")
	@GetMapping(path = "/topics/despedida/{nombre}")
	public String despedida(@PathVariable String nombre) {
		String msg = "Adios " + nombre;
		jms.setPubSubDomain(true);
		jms.convertAndSend("despedidas", new MessageDTO(msg, origen));
		return "SEND TEMA: " + msg;
	}

	@Tag(name = "Topics")
	@GetMapping(path = "/topics/rafaga")
	public String despedida(@RequestParam(defaultValue = "10") int cantidad) {
		jms.setPubSubDomain(true);
		if(cantidad <= 0) cantidad = 1;
		for(int i=0; i < cantidad; i++)
			jms.convertAndSend("despedidas", new MessageDTO("Adios " + faker.name().firstName(), origen));
		return "SEND TEMA: %d mensajes".formatted(cantidad);
	}
	
}
