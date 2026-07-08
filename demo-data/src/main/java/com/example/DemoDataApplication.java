package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.example.contracts.domain.repositories.ActorsRepository;
import com.example.domain.entities.Actor;

@SpringBootApplication
public class DemoDataApplication {
	@Autowired @Lazy DemoDataApplication self;
	
	public static void main(String[] args) {
		SpringApplication.run(DemoDataApplication.class, args);
	}

	@Bean
	CommandLineRunner consultas(ActorsRepository dao) {
		return arg -> {
			//dao.findAll().forEach(IO::println);
			//dao.findTop5ByFirstNameStartingWithOrderByLastNameDesc("P").forEach(IO::println);
			//dao.findTop5ByFirstNameStartingWith("P", Sort.by("FirstName").descending()).forEach(IO::println);
//			dao.findByActorIdGreaterThanEqual(197).forEach(IO::println);
//			dao.findNovedadesJPQL(197).forEach(IO::println);
//			dao.findNovedadesSQL(197).forEach(IO::println);
//			dao.findAll((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("actorId"), 197)).forEach(IO::println);
//			dao.findAll((root, query, builder) -> builder.greaterThan(root.get("actorId"), 199)).forEach(IO::println);
			self.relaciones(dao);
		};
	}
	
//	@Transactional
	void relaciones(ActorsRepository dao) {
		dao.findByActorIdGreaterThanEqual(199).forEach(item -> {
			IO.println(item);
			item.getFilmActors().forEach(peli -> {
				IO.println("%d -> %s".formatted(peli.getFilm().getFilmId(), peli.getFilm().getTitle()));
			});
		});
	}

//	@Bean
	CommandLineRunner crud(ActorsRepository dao) {
		return arg -> {
			var actor = dao.save(new Actor("Pepito", "Grillo"));
			var id = actor.getActorId();
			var leido = dao.findById(id);
			if(leido.isEmpty()) {
				System.err.println("Actor no encontrado");
				return;
			}
			actor = leido.get();
			actor.setFirstName(actor.getFirstName().toUpperCase());
			dao.save(actor);
			dao.findAll().forEach(System.out::println);
			dao.deleteById(id);
			if(!dao.existsById(id))
				System.err.println("Actor %d no encontrado".formatted(id));
			dao.deleteById(id);
		};
	}
}
