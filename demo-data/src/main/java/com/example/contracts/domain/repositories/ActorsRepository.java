package com.example.contracts.domain.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

import com.example.domain.entities.Actor;
import com.example.domain.entities.models.ActorDTO;
import com.example.domain.entities.models.ActorShort;

public interface ActorsRepository extends JpaRepository<Actor, Integer>, JpaSpecificationExecutor<Actor> {
	List<Actor> findTop5ByFirstNameStartingWithOrderByLastNameDesc(String prefijo);
	List<Actor> findTop5ByFirstNameStartingWith(String prefijo, Sort orderBy);
	
	@EntityGraph(attributePaths = {"filmActors.film"})
	List<Actor> findByActorIdGreaterThanEqual(int id);
	@Query("from Actor a where a.actorId >= ?1")
	List<Actor> findNovedadesJPQL(int id);
	@NativeQuery("select * from actor a where a.actor_id >= :id")
	List<Actor> findNovedadesSQL(int id);
	
	List<ActorDTO> readByActorIdGreaterThanEqual(int id);
	List<ActorShort> queryByActorIdGreaterThanEqual(int id);
	
	<T> List<T> getByActorIdGreaterThanEqual(int id, Class<T> tipo);

}
