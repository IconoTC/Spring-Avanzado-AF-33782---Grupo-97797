package com.example.ioc;

import java.beans.ConstructorProperties;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Lazy
@Component("miOtraClase")
//@Scope("prototype")
public class ConstructorConValores {
	@Autowired
	private NotificationService notify;
	private String autorString;
	
	@ConstructorProperties({"version", "otroAutor"})
	public ConstructorConValores(int version, String otroAutor /*, NotificationService notify*/) {
//		this.notify = notify;
//		notify.add(getClass().getSimpleName() + " - Version: " + version + " Autor: " + otroAutor);
		System.err.println(notify == null ? "no se ha inyectado": ("se ha inyectado "));
		System.err.println(getClass().getSimpleName() + " - Version: " + version + " Autor: " + otroAutor);
		autorString = otroAutor;
	}
	@PostConstruct
	void init() {
		System.err.println(notify == null ? "no se ha inyectado": ("se ha inyectado " + notify.getClass().getSimpleName()));
//		notify.add(getClass().getSimpleName() + " - Version: " + version + " Autor: " + otroAutor);
	}
	
	public String getAutor() {
		return autorString;
	}
	
	public void titulo(String tratamiento, String autor) {
		System.err.println(tratamiento.toUpperCase() + " " + autor.toUpperCase());
	}
	public void titulo(String autor) {
		System.err.println(autor.toUpperCase());
	}

	@PreDestroy
	void fin() {
		System.err.println("me destruye");
	}
////	public String dameValor(String autor) {
////		return Optional.empty();		
////	}
//
//	public Optional<String> dameValor(String autor) {
//		return Optional.empty();		
//	}
}
