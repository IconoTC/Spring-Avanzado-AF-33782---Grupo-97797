package com.example.domain.entities.models;

import com.example.domain.entities.Actor;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ActorDTO {
	private int id;
	@JsonProperty("nombre")
	private String firstName;
	@JsonProperty("apellidos")
	private String lastName;

	public static ActorDTO from(Actor source) {
		return new ActorDTO(
				source.getId(), 
				source.getFirstName(), 
				source.getLastName()
				);
	}
	public static Actor from(ActorDTO source) {
		return new Actor(
				source.getId(), 
				source.getFirstName(), 
				source.getLastName()
				);
	}
}
