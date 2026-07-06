package com.example.ioc.implementaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

import com.example.ioc.NotificationService;
import com.example.ioc.contratos.Configuracion;
import com.example.ioc.contratos.RepositorioCadenas;

@Repository
@Profile({"prod", "default"})
//@Primary
//@Qualifier("prod")
public class RepositorioCadenasImpl implements RepositorioCadenas {
	private final Configuracion configuracion;
	private final NotificationService notify;
	
	public RepositorioCadenasImpl(Configuracion configuracion, NotificationService notify) {
		this.notify = notify;
		notify.add(getClass().getSimpleName() + " Constructor");
		this.configuracion = configuracion;
		configuracion.config();
	}

	@Override
//	@Logging
	public String load() {
		var contador = configuracion.getNext();
		return "Cadena leida de la base de datos. Me han usado " + contador + (contador == 1 ? " vez." : " veces.");
	}


	@Override
//	@Logged
	public void save(String item) {
		notify.add("Guardo los guardo los datos '%s' con %s".formatted(item, getClass().getSimpleName()));
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