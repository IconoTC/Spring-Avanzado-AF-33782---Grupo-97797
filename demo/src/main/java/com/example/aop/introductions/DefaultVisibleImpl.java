package com.example.aop.introductions;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class DefaultVisibleImpl implements Visible {
	private int count = 0;

	@Override
	public void mostrar() {
		count++;
	}

	@Override
	public void ocultar() {
		count = count > 0 ? (count - 1) : 0;
	}

	@Override
	public boolean isVisible() {
		return count > 0;
	}
}
