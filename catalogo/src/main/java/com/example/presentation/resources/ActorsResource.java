package com.example.presentation.resources;

import java.net.URI;
import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.contracts.domain.services.ActorsService;
import com.example.core.contracts.domain.exceptions.BadRequestException;
import com.example.core.contracts.domain.exceptions.DuplicateKeyException;
import com.example.core.contracts.domain.exceptions.InvalidDataException;
import com.example.core.contracts.domain.exceptions.NotFoundException;
import com.example.domain.entities.models.ActorDTO;
import com.example.domain.entities.models.ActorShort;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/actores")
public class ActorsResource {
	private ActorsService srv;

	public ActorsResource(ActorsService srv) {
		this.srv = srv;
	}

	@GetMapping(path = "/v1")
	public List<?> getAll(@Parameter(in = ParameterIn.QUERY, name = "modo", required = false, description = "Formato del actor", 
		schema = @Schema(type = "string", allowableValues = {"corto", "largo" }, defaultValue = "corto")) 
		@RequestParam(required = false, defaultValue = "corto") String modo) {
		if ("corto".equals(modo))
			return (List<?>) srv.getByProjection(Sort.by("firstName", "lastName"), ActorShort.class);
		else
			return (List<?>) srv.getByProjection(Sort.by("firstName", "lastName"), ActorDTO.class); // srv.getAll();;
	}

	@GetMapping(path = "/v2")
	public List<ActorDTO> getAllv2(@RequestParam(required = false) String sort) {
		if (sort != null)
			return (List<ActorDTO>) srv.getByProjection(Sort.by(sort), ActorDTO.class);
		return srv.getByProjection(ActorDTO.class);
	}

	@GetMapping(path = { "/v1", "/v2" }, params = "page")
	public PagedModel<ActorShort> getAll(@ParameterObject Pageable page) {
		return new PagedModel<ActorShort>(srv.getByProjection(page, ActorShort.class));
	}

	@GetMapping(path = "/v1/{id}")
	public ActorDTO getOneSinETag(@PathVariable int id) throws NotFoundException {
		var item = srv.getOne(id);
		if (item.isEmpty())
			throw new NotFoundException();
		return ActorDTO.from(item.get());
	}

	@GetMapping(path = { "/v2/{id}" })
	public ResponseEntity<ActorDTO> getOne(@PathVariable int id) throws NotFoundException {
		var item = srv.getOne(id);
		if (item.isEmpty())
			throw new NotFoundException();
		var dto = ActorDTO.from(item.get());
		return ResponseEntity.ok().eTag(ETagTools.generate(dto)).body(dto);
	}

	record Filmografia(int id, String titulo, Short año) {
	}

	@GetMapping(path = { "/v1/{id}/filmografia", "/v2/{id}/filmografia" })
	@Transactional
	public List<Filmografia> getPelis(@PathVariable int id) throws NotFoundException {
		var item = srv.getOne(id);
		if (item.isEmpty())
			throw new NotFoundException();
		return item.get().getFilmActors().stream().map(
				o -> new Filmografia(o.getFilm().getId(), o.getFilm().getTitle(), o.getFilm().getReleaseYear()))
				.sorted((a, b) -> {
					if (a.año != null) {
						int cmp = a.año.compareTo(b.año);
						if (cmp != 0)
							return cmp;
					}
					return a.titulo.compareTo(b.titulo);
				}).toList();
	}

	@DeleteMapping(path = "/v1/{id}/jubilacion")
	@ResponseStatus(HttpStatus.ACCEPTED)
	@SecurityRequirement(name = "bearerAuth")
	public void jubilar(@PathVariable int id) throws NotFoundException {
		var item = srv.getOne(id);
		if (item.isEmpty())
			throw new NotFoundException();
		item.get().jubilate();
	}

	@PostMapping(path = { "/v1", "/v2" })
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<Object> create(@Valid @RequestBody ActorDTO item)
			throws BadRequestException, DuplicateKeyException, InvalidDataException {
		var newItem = srv.add(ActorDTO.from(item));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newItem.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping(path = "/v1/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@SecurityRequirement(name = "bearerAuth")
	public void updateSinETag(@PathVariable int id, @Valid @RequestBody ActorDTO item)
			throws NotFoundException, InvalidDataException, BadRequestException {
		if (id != item.getId())
			throw new BadRequestException("No coinciden los identificadores");
		srv.modify(ActorDTO.from(item));
	}

	@PutMapping(path = { "/v2/{id}" })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@SecurityRequirement(name = "bearerAuth")
	public void update(@PathVariable int id, @Valid @RequestBody ActorDTO item, @RequestHeader(value = HttpHeaders.IF_MATCH, required = true) String ifMatch)
			throws NotFoundException, InvalidDataException, BadRequestException {
		if (id != item.getId())
			throw new BadRequestException("No coinciden los identificadores");
		if(ETagTools.ifMatch(ActorDTO.from(srv.getOne(id).orElseThrow(() -> new NotFoundException())), ifMatch))
			srv.modify(ActorDTO.from(item));
		else
			throw new ResponseStatusException(HttpStatusCode.valueOf(412));
	}

	@DeleteMapping(path = "/v1/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@SecurityRequirement(name = "bearerAuth")
	public void deleteSinETag(@PathVariable int id) {
		srv.deleteById(id);
	}

	@DeleteMapping(path = { "/v2/{id}" })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@SecurityRequirement(name = "bearerAuth")
	public void delete(@PathVariable int id, @RequestHeader(value = HttpHeaders.IF_MATCH, required = true) String ifMatch) {
		if(ETagTools.ifMatch(ActorDTO.from(srv.getOne(id).orElseThrow(() -> new NotFoundException())), ifMatch))
			srv.deleteById(id);
		else
			throw new ResponseStatusException(HttpStatusCode.valueOf(412));
	}
}
