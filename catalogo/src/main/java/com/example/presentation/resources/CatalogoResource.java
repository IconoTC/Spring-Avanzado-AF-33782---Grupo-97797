package com.example.presentation.resources;

import java.time.LocalDateTime;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.application.models.NovedadesDTO;
import com.example.contracts.application.CatalogoService;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Value;

@RestController
@RequestMapping(path = "/")
public class CatalogoResource {
	@Autowired
	private CatalogoService srv;

	@Value
	public static class CatalogoResources {
		@Value
		public class CatalogoLinks {
			public class Href {
				private String href;
				public String getHref() { return href; }
				public Href(String path) {
					href = ServletUriComponentsBuilder.fromCurrentRequest().path(path).toUriString();
				}
			}
			private Href self = new Href("");
			private Href actores = new Href("/actores/v1");
			private Href peliculas = new Href("/peliculas/v1");
			private Href categorias = new Href("/categorias/v1");
			private Href idiomas = new Href("/idiomas/v1");
			private Href novedades = new Href("/novedades/v1");
			@JsonProperty("documentacion-swagger")
			private Href swagger = new Href("/open-api");
			@JsonProperty("documentacion-scalar")
			private Href scalar = new Href("/scalar");
		}

		@JsonProperty("_links")
		private CatalogoLinks links = new CatalogoLinks();
	}

	@GetMapping(path = "/")
	public ResponseEntity<CatalogoResources> getResources() {
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/hal+json").body(new CatalogoResources());
	}
	
	@GetMapping(path = "/novedades/v1")
	public NovedadesDTO novedades(@Parameter(example = "2021-01-01 00:00:00") @RequestParam(required = false, defaultValue = "2021-01-01 00:00:00") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") LocalDateTime fecha) {
		if(fecha == null)
			fecha = LocalDateTime.now().minusDays(1);
		return srv.novedades(fecha);
	}

}
