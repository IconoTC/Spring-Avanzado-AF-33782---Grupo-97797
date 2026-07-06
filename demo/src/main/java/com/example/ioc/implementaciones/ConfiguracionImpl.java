package com.example.ioc.implementaciones;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.ioc.NotificationService;
import com.example.ioc.contratos.Configuracion;

@Component
@Scope("prototype")
public class ConfiguracionImpl implements Configuracion {
	private final NotificationService notify;
	private int contador = 0;
	
	public ConfiguracionImpl(NotificationService notify) {
		this.notify = notify;
		notify.add(getClass().getSimpleName() + " Constructor");
	}

	@Override
	public void config() {
		notify.add("Me configuran");	
	}

	@Override
	public int getNext() {
		return ++contador;
	}

}
