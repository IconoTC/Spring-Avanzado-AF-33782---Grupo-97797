package com.example.ioc.implementaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

import com.example.ioc.NotificationService;
import com.example.ioc.anotaciones.MockRepository;
//import com.example.ioc.anotaciones.RepositoryMock;
import com.example.ioc.contratos.RepositorioCadenas;

import jakarta.annotation.PostConstruct;

//@MockRepository
//@Repository("dao")
//@Qualifier("test")
//@Primary
public class RepositorioCadenasMock implements RepositorioCadenas {
	@Autowired
	private NotificationService notify;
	
	public RepositorioCadenasMock() {
//		notify.add(getClass().getSimpleName() + " Constructor");
	}
	@PostConstruct
	private void init() {
		notify.add(getClass().getSimpleName() + " Constructor");
	}

	@Override
	public String load() {
		return "Simulación de una cadena leida";
	}


	@Override
//	@LoggerAll
	public void save(String item) {
		notify.add("Simulo que guardo los datos '%s' con %s".formatted(item, getClass().getSimpleName()));
		doEvent("Han ejecutado el guardar.");
		
	}	
	
	@Autowired(required = false) 
	private ApplicationEventPublisher publisher;
	public void setPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}
	protected void doEvent(@NonNull String event) { 
		if(publisher != null)
			publisher.publishEvent(event); 
	}
}