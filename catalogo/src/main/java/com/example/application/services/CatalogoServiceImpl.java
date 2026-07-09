package com.example.application.services;

import java.time.LocalDateTime;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.models.NovedadesDTO;
import com.example.contracts.application.CatalogoService;
import com.example.contracts.domain.services.ActorsService;
import com.example.contracts.domain.services.CategoriesService;
import com.example.contracts.domain.services.FilmsService;
import com.example.contracts.domain.services.LanguagesService;
import com.example.domain.entities.models.ActorDTO;
import com.example.domain.entities.models.FilmShort;

@Service
public class CatalogoServiceImpl implements CatalogoService {
	@Autowired
	private FilmsService filmSrv;
	@Autowired
	private ActorsService artorSrv;
	@Autowired
	private CategoriesService categorySrv;
	@Autowired
	private LanguagesService languageSrv;

	@Override
	public NovedadesDTO novedades(LocalDateTime fecha) {
		// LocalDateTime fecha = Date.valueOf("2019-01-01 00:00:00");
		if(fecha == null)
			fecha = LocalDateTime.now().minusDays(1);
		return new NovedadesDTO(
				filmSrv.novedades(fecha).stream().map(item -> new FilmShort(item.getId(), item.getTitle())).toList(), 
				artorSrv.novedades(fecha).stream().map(item -> ActorDTO.from(item)).toList(), 
				categorySrv.novedades(fecha), 
				languageSrv.novedades(fecha)
				);
	}

}
