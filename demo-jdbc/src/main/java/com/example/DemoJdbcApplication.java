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
	
	@Bean
	CommandLineRunner consultas(JdbcClient jdbcClient) {
		return arg -> {
			IO.println("número de actores: %d".formatted(
					jdbcClient.sql("select count(1) from actor")
						.query(Integer.class)
						.single()
					));
			
			var item = jdbcClient.sql("select * from actor where actor_id = ?")
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
}
