package com.example.base;

import java.util.Optional;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

// import lombok.NonNull;

@NullMarked
public class DummyJSpecify {
	@Nullable String cadena;

	
	public DummyJSpecify() {
		cadena = null;
	}
	
	public DummyJSpecify(String cadena) {
//		this.cadena = cadena;
		setCadenaSegura(cadena);
	}

	public @Nullable String getCadena() {
		return cadena;
	}

	public boolean hasCadena() {
		return cadena != null;
	}

	public Optional<String> getCadenaSegura() {
		return Optional.ofNullable(cadena);
	}

	public void setCadena(String cadena) {
		this.cadena = cadena;
	}

	public void setCadenaSegura(@Nullable String cadena) {
		if(cadena == null)
			throw new IllegalArgumentException("El argumento no puede ser nulo");
		this.cadena = cadena;
	}
	
	public void clearCadena() {
		this.cadena = null;
	}
	
}
