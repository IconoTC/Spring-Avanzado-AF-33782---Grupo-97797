package com.example.ioc.notificaciones;

import org.springframework.stereotype.Component;

@Component
public class TwitterSender implements Sender {

	@Override
	public void send(String message) {
		System.err.println("Twiteo: " + message);
	}

}
