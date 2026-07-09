package com.example.domain.entities.models;

import com.example.domain.entities.Actor;

import lombok.Data;

@Data
public class ActorDTO {
	private int id;
	private String nombre;
	private String apellidos;
	
	public ActorDTO(int actorId, String firstName, String lastName) {
		this.id = actorId;
		this.nombre = firstName;
		this.apellidos = lastName;
	}
	
	public static ActorDTO from(Actor target) {
		return new ActorDTO(
				target.getActorId(), 
				target.getFirstName(), 
				target.getLastName()
				);
	}
	
	public static Actor from(ActorDTO target) {
		return new Actor(
				target.getId(), 
				target.getNombre(), 
				target.getApellidos()
				);
	}
}
