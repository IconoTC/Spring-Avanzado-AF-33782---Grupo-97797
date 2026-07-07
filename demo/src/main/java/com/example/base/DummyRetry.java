package com.example.base;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.retry.RetryPolicy;
import org.springframework.core.retry.RetryTemplate;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class DummyRetry {
	private static final Logger log = LoggerFactory.getLogger(DummyRetry.class);
	private int intentos = 0;
	
	private int reintenta(int acierta) {
		if(++intentos % acierta != 0) {
			var msg = "%d intento de %d".formatted(intentos, acierta);
			log.error(msg);
			throw new NoSuchElementException(msg);
		}
		var result = intentos;
		intentos = 0;
		return result;
	}
	
	@Retryable
	public int reintentaConAnotacion(int acierta) {
		return reintenta(acierta);
	}
	
	public int reintentaConTemplate(int acierta) {
		final var result = new Object() { public int valor = 0; };
		var retryTemplate = new RetryTemplate(RetryPolicy.withMaxRetries(3)); 
		retryTemplate.invoke(() -> { result.valor = reintenta(acierta); });		
		return result.valor;
	}
	
	public void reinicia() {
		intentos = 0;
	}
	

}
