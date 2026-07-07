package com.example.base;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.resilience.annotation.ConcurrencyLimit.ThrottlePolicy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class DummyAsync {
    @Autowired @Lazy
    private DummyAsync self;

	public void ejecutarAutoInvocado(int i) {
		self.ejecutarTareaSimpleAsync(i);
	}
	
	@Async
	public void ejecutarTareaSimpleAsync(int i) {
		ejecutarTareaSimple(i);
	}
	
	public void ejecutarTareaSimple(int i) {
		System.err.println(
				"-> Tarea simple %d ejecutándose en el hilo: %s.".formatted(i, Thread.currentThread().getName()));
		try {
			Thread.sleep(2000 - i * 100);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		System.err.println("<- Tarea simple %d finalizada.".formatted(i));
	}

	@Async
	@ConcurrencyLimit(limit = 5, policy = ThrottlePolicy.BLOCK)
	public CompletableFuture<String> calcularResultadoAsync(int... input) {
		return calcularResultado(input);
	}
	
	public CompletableFuture<String> calcularResultado(int... input) {
		System.err.println(
				"-> Cálculo para %s iniciando en el hilo: %s.".formatted(Arrays.toString(input), Thread.currentThread().getName()));
		if(input == null || input.length == 0) {
			System.err.println("<- Cálculo [] terminado.".formatted(Arrays.toString(input)));
			return CompletableFuture.completedFuture("No hay datos");
		}
		var result = 0;
		try {
			for (int num : input) {
				result += num;
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		System.err.println("<- Cálculo de %s terminado.".formatted(Arrays.toString(input)));
		return CompletableFuture.completedFuture("La media de %s es %1.2f".formatted(Arrays.toString(input), (double)result / input.length));
	}
}
