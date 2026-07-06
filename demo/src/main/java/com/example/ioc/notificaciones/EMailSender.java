package com.example.ioc.notificaciones;

import org.springframework.stereotype.Component;

@Component("email")
public class EMailSender implements Sender {

	@Override
	public void send(String message) {
		System.err.println("Envio correo: " + message);
	}

}
