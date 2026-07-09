package com.example.domain.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Embeddable
public class FilmRental {
	@NotNull
	@Positive
	@Column(name = "rental_duration", nullable = false)
	private byte duration;

	@NotNull
	@Digits(integer = 2, fraction = 2)
	@DecimalMin(value = "0.0", inclusive = false)
	@Column(name = "rental_rate", nullable = false, precision = 10, scale = 2)
	private BigDecimal rate;

	public FilmRental() { }
	
	public FilmRental(byte duration, BigDecimal rate) {
		this.duration = duration;
		this.rate = rate;
	}

	public byte getDuration() {
		return duration;
	}

	public void setDuration(byte duration) {
		this.duration = duration;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "Rental [duration=" + duration + ", rate=" + rate + "]";
	}
	
}
