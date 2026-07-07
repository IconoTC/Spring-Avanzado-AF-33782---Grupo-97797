package com.example.ioc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
//@Scope("prototype")
public class NotificationServiceImpl implements NotificationService {
	private List<String> listado = new ArrayList<>();
	
	@Override
	public boolean hasMessages() {
		return listado.size() > 0;
	}

	@Override
	public List<String> getListado() {
		return listado.stream().toList();
	}
	
	@Override
	public Optional<String> getMessage(int index) {
		if( 0 > index || index >= listado.size())
			return Optional.empty();
		return Optional.of(listado.get(index));
	}

	@Override
	public void add(String message) {
		if(message == null || message.trim() == "")
			throw new IllegalArgumentException("Falta el mensaje.");
		listado.add(message);
		doEvent("Añadido el mensaje: " + message);
	}
	@Override
	public void delete(int index) {
		if( 0 > index || index >= listado.size())
			throw new IndexOutOfBoundsException();
		listado.remove(index);
		doEvent("Borrado el mensaje " + index);
	}
	@Override
	public void clear() {
		listado.clear();
		doEvent("Borrados todos los mensajes.");
	}

	private ApplicationEventPublisher publisher;
	@Autowired(required = false) 
	public void setPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	protected void doEvent(@NonNull String event) { 
		if(publisher == null) return;
		publisher.publishEvent(new GenericoEvent(getClass().getSimpleName(), event)); 
	}
	
	@EventListener
	private void eventHandler(String event) {
		add("Evento cadena: %s".formatted(event));
	}
	
}
