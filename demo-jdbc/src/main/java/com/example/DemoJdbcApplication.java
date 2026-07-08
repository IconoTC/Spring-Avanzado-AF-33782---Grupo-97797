package com.example;

import java.sql.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.simple.JdbcClient;

import lombok.Data;

@SpringBootApplication
public class DemoJdbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoJdbcApplication.class, args);
	}

	@Data
	static class Actor {
		private int actorId;
		private String firstName;
		private String lastName;
		private Date lastUpdate;
	}
	
//	@Bean
	CommandLineRunner consultas(JdbcClient jdbcClient) {
		return arg -> {
			IO.println("número de actores: %d".formatted(
					jdbcClient
						.sql("select count(1) from actor")
						.query(Integer.class)
						.single()
					));
			
			var item = jdbcClient
					.sql("select * from actor where actor_id = ?")
					.param(1000)
					.query(Actor.class)
					.optional();
			if(item.isPresent())
				IO.println(item.get());
			else {
				IO.println("Actor no encontrado");
			}
			jdbcClient.sql("select * from actor where actor_id <= ?")
				.param(10)
				.query(Actor.class)
				.list()
				.forEach(IO::println);
		};
	}
	
	@Bean
	CommandLineRunner dml(JdbcClient jdbcClient) {
		return arg -> {
			var rows = jdbcClient
					.sql("insert into actor (first_name, last_name) values (?, ?)")
					.param("Leonor")
					.param("Watling")
					.update();
			IO.println("Filas insertadas: " + rows);
			int id = jdbcClient
					.sql("select actor_id from actor where first_name = :firstName and last_name = :lastName")
					.param("lastName", "Watling")
					.param("firstName", "Leonor")
					.query(Integer.class)
					.single();
			var actor = jdbcClient
					.sql("select * from actor where actor_id = ?")
					.param(id)
					.query(Actor.class)
					.single();
			IO.println(actor);
			rows = jdbcClient.sql("update actor set first_name = :firstName, last_name = :lastName where actor_id = :id")
					.param("id", actor.getActorId())
					.param("firstName", actor.getFirstName().toUpperCase())
					.param("lastName", actor.getLastName().toUpperCase())
					.update();
			IO.println("Filas modificadas: " + rows);
			IO.println(actor);
			actor = jdbcClient.sql("select * from actor where actor_id = ?")
					.param(id)
					.query(Actor.class)
					.single();
			IO.println(actor);
			rows = jdbcClient
					.sql("delete from actor where actor_id = ?")
					.param(actor.getActorId())
					.update();
			IO.println("Filas borradas: " + rows);
			if(jdbcClient.sql("select * from actor where actor_id = ?")
					.param(id)
					.query(Actor.class)
					.optional()
					.isEmpty()) {
				IO.println("No encontrado");
			} else {
				IO.println("No lo ha borrado");
			}
		};
	}
}
