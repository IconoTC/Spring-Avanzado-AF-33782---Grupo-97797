package com.example.domain.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.contracts.domain.repositories.LanguagesRepository;
import com.example.contracts.domain.services.LanguagesService;
import com.example.core.contracts.domain.exceptions.DuplicateKeyException;
import com.example.core.contracts.domain.exceptions.InvalidDataException;
import com.example.core.contracts.domain.exceptions.NotFoundException;
import com.example.domain.entities.Language;

@Service
public class LanguagesServiceImpl implements LanguagesService {
	private LanguagesRepository dao;

	public LanguagesServiceImpl(LanguagesRepository dao) {
		this.dao = dao;
	}

	@Override
	public List<Language> getAll() {
		return dao.findAll();
	}

	@Override
	public Optional<Language> getOne(Integer id) {
		return dao.findById(id);
	}

	@Override
	public Language add(Language item) throws DuplicateKeyException, InvalidDataException {
		if(item == null)
			throw new InvalidDataException("No puede ser nulo");
		if(item.isInvalid())
			throw new InvalidDataException(item.getErrorsMessage(), item.getErrorsFields());
		if(item.getId() != 0 && dao.existsById(item.getId()))
			throw new DuplicateKeyException("Ya existe");
		return dao.save(item);
	}

	@Override
	public Language modify(Language item) throws NotFoundException, InvalidDataException {
		if(item == null)
			throw new InvalidDataException("No puede ser nulo");
		if(item.isInvalid())
			throw new InvalidDataException(item.getErrorsMessage(), item.getErrorsFields());
		if(!dao.existsById(item.getId()))
			throw new NotFoundException();
		return dao.save(item);
	}

	@Override
	public void delete(Language item) throws InvalidDataException {
		if(item == null)
			throw new InvalidDataException("No puede ser nulo");
		dao.delete(item);
	}

	@Override
	public void deleteById(Integer id) {
		dao.deleteById(id);
	}

	@Override
	public List<Language> novedades(LocalDateTime fecha) {
		return dao.findByLastUpdateGreaterThanEqualOrderByLastUpdate(fecha);
	}
	
}
