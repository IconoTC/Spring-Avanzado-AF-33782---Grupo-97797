package com.example.domain.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.contracts.domain.repositories.CategoriesRepository;
import com.example.contracts.domain.services.CategoriesService;
import com.example.core.contracts.domain.exceptions.DuplicateKeyException;
import com.example.core.contracts.domain.exceptions.InvalidDataException;
import com.example.core.contracts.domain.exceptions.NotFoundException;
import com.example.domain.entities.Category;

@Service
public class CategoriesServiceImpl implements CategoriesService {
	private CategoriesRepository dao;

	public CategoriesServiceImpl(CategoriesRepository dao) {
		this.dao = dao;
	}

	@Override
	public List<Category> getAll() {
		return dao.findAllByOrderByName();
	}

	@Override
	public Optional<Category> getOne(Integer id) {
		return dao.findById(id);
	}

	@Override
	public Category add(Category item) throws DuplicateKeyException, InvalidDataException {
		if(item == null)
			throw new InvalidDataException("No puede ser nulo");
		if(item.isInvalid())
			throw new InvalidDataException(item.getErrorsMessage(), item.getErrorsFields());
//		if(item.getId() != 0 && dao.existsById(item.getId()))
//			throw new DuplicateKeyException("Ya existe");
//		return dao.save(item);
		return dao.insert(item);
	}

	@Override
	public Category modify(Category item) throws NotFoundException, InvalidDataException {
		if(item == null)
			throw new InvalidDataException("No puede ser nulo");
		if(item.isInvalid())
			throw new InvalidDataException(item.getErrorsMessage(), item.getErrorsFields());
//		if(!dao.existsById(item.getId()))
//			throw new NotFoundException();
//		return dao.save(item);
		return dao.update(item);
	}

	@Override
	public void delete(Category item) throws InvalidDataException {
		if(item == null)
			throw new InvalidDataException("No puede ser nulo");
		dao.delete(item);
	}

	@Override
	public void deleteById(Integer id) {
		dao.deleteById(id);
	}

	@Override
	public List<Category> novedades(LocalDateTime fecha) {
		return dao.findByLastUpdateGreaterThanEqualOrderByLastUpdate(fecha);
	}
	
}
