package com.example.ioc.notificaciones;

import org.springframework.stereotype.Component;

@Component
public class FileSender implements Sender {

	@Override
	public void send(String message) {
		System.err.println("Escribo en el fichero: " + message);
	}

}
