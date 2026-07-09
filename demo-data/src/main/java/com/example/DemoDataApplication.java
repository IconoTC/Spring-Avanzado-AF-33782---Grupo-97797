package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.example.contracts.domain.repositories.ActorsRepository;
import com.example.contracts.domain.repositories.CategoriesRepository;
import com.example.domain.entities.Actor;
import com.example.domain.entities.models.ActorDTO;
import com.example.domain.entities.models.ActorShort;

import tools.jackson.databind.json.JsonMapper;
import tools.jackson.dataformat.xml.XmlMapper;

@SpringBootApplication
public class DemoDataApplication {
	@Autowired
	@Lazy
	DemoDataApplication self;

	public static void main(String[] args) {
		SpringApplication.run(DemoDataApplication.class, args);
	}

	@Bean
	CommandLineRunner consultas(ActorsRepository dao) {
		return arg -> {
			// dao.findAll().forEach(IO::println);
			// dao.findTop5ByFirstNameStartingWithOrderByLastNameDesc("P").forEach(IO::println);
			// dao.findTop5ByFirstNameStartingWith("P",
			// Sort.by("FirstName").descending()).forEach(IO::println);
//			dao.findByActorIdGreaterThanEqual(197).forEach(IO::println);
//			dao.findNovedadesJPQL(197).forEach(IO::println);
//			dao.findNovedadesSQL(197).forEach(IO::println);
//			dao.findAll((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("actorId"), 197)).forEach(IO::println);
//			dao.findAll((root, query, builder) -> builder.greaterThan(root.get("actorId"), 199)).forEach(IO::println);
//			self.relaciones(dao);
			try {
				self.modifica(dao);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}

	record ActorDetail(int actorId, String firstName, String lastName) {
	}

//	@Bean
	CommandLineRunner proyecciones(ActorsRepository dao) {
		return arg -> {
//			dao.findByActorIdGreaterThanEqual(200).forEach(IO::println);
//			dao.findByActorIdGreaterThanEqual(200).forEach(item -> IO.println(ActorDTO.from(item)));
//			dao.readByActorIdGreaterThanEqual(200).forEach(IO::println);
//			dao.queryByActorIdGreaterThanEqual(200).forEach(item -> IO.println("%d %s".formatted(item.getId(), item.getNombre())));
			dao.getByActorIdGreaterThanEqual(200, ActorDTO.class).forEach(IO::println);
			dao.getByActorIdGreaterThanEqual(200, ActorShort.class)
					.forEach(item -> IO.println("%d %s".formatted(item.getId(), item.getNombre())));
			dao.getByActorIdGreaterThanEqual(200, ActorDetail.class).forEach(IO::println);
		};
	}

	@Bean
	@Transactional
	CommandLineRunner serializacion(CategoriesRepository dao, JsonMapper toJson, XmlMapper toXml) {
		return arg -> {
//			var item = dao.findById(1).get();
//			IO.println(toJson.writeValueAsString(item));
//			IO.println(toXml.writeValueAsString(item));
//			IO.println(toJson.writeValueAsString(dao.findAll()));
//			IO.println(toXml.writeValueAsString(dao.findAll()));
			self.serializacionDependencia(dao, toJson, toXml);
		};
	}

	@Transactional
	void serializacionDependencia(CategoriesRepository dao, JsonMapper toJson, XmlMapper toXml) {
		var item = dao.findById(1).get();
		IO.println(toJson.writeValueAsString(item));
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

	@Transactional(rollbackFor = Exception.class)
	void modifica(ActorsRepository dao) {
		var actor = new Actor(null, "4g");
		if (actor.isInvalid()) {
			System.err.println(actor.getErrorsMessage());
		} else {
			dao.save(actor);
		}
//		dao.save(new Actor("Pepito", "Grillo"));
//		dao.save(new Actor("Carmelo", "Coton"));
//		dao.deleteById(1);
	}

//	@Bean
	CommandLineRunner crud(ActorsRepository dao) {
		return arg -> {
			var actor = dao.save(new Actor("Pepito", "Grillo"));
			var id = actor.getActorId();
			var leido = dao.findById(id);
			if (leido.isEmpty()) {
				System.err.println("Actor no encontrado");
				return;
			}
			actor = leido.get();
			actor.setFirstName(actor.getFirstName().toUpperCase());
			dao.save(actor);
			dao.findAll().forEach(System.out::println);
			dao.deleteById(id);
			if (!dao.existsById(id))
				System.err.println("Actor %d no encontrado".formatted(id));
			dao.deleteById(id);
		};
	}
}
