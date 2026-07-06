package com.example.ioc.notificaciones;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component("email")
@Profile({"default", "test"})
public class EMailSender implements Sender {

	@Override
	public void send(String message) {
		System.err.println("Envio correo: " + message);
	}

}
