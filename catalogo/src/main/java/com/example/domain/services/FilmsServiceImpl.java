package com.example.domain.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.contracts.domain.repositories.FilmsRepository;
import com.example.contracts.domain.services.FilmsService;
import com.example.core.contracts.domain.exceptions.DuplicateKeyException;
import com.example.core.contracts.domain.exceptions.InvalidDataException;
import com.example.core.contracts.domain.exceptions.NotFoundException;
import com.example.domain.entities.Film;

import jakarta.transaction.Transactional;
import lombok.NonNull;

@Service
public class FilmsServiceImpl implements FilmsService {
	private FilmsRepository dao;

	public FilmsServiceImpl(FilmsRepository dao) {
		this.dao = dao;
	}

	@Override
	public <T> List<T> getByProjection(@NonNull Class<T> type) {
		return dao.findAllBy(type);
	}

	@Override
	public <T> List<T> getByProjection(@NonNull Sort sort, @NonNull Class<T> type) {
		return dao.findAllBy(sort, type);
	}

	@Override
	public <T> Page<T> getByProjection(@NonNull Pageable pageable, @NonNull Class<T> type) {
		return dao.findAllBy(pageable, type);
	}

	@Override
	public List<Film> getAll(@NonNull Sort sort) {
		return dao.findAll(sort);
	}

	@Override
	public Page<Film> getAll(@NonNull Pageable pageable) {
		return dao.findAll(pageable);
	}

	@Override
	public List<Film> getAll() {
		return dao.findAll();
	}

	@Override
	public Optional<Film> getOne(Integer id) {
		return dao.findById(id);
	}

	@Override
	public Optional<Film> getOne(@NonNull Specification<Film> spec) {
		return dao.findOne(spec);
	}

	@Override
	public List<Film> getAll(@NonNull Specification<Film> spec) {
		return dao.findAll(spec);
	}

	@Override
	public Page<Film> getAll(@NonNull Specification<Film> spec, @NonNull Pageable pageable) {
		return dao.findAll(spec, pageable);
	}

	@Override
	public List<Film> getAll(@NonNull Specification<Film> spec, @NonNull Sort sort) {
		return dao.findAll(spec, sort);
	}

	@Override
	@Transactional
	public Film add(Film item) throws DuplicateKeyException, InvalidDataException {
		if(item == null)
			throw new InvalidDataException("No puede ser nulo");
		if(item.isInvalid())
			throw new InvalidDataException(item.getErrorsMessage(), item.getErrorsFields());
		if(dao.existsById(item.getId()))
			throw new DuplicateKeyException(item.getErrorsMessage());
		return dao.save(item);
	}

	@Override
	@Transactional
	public Film modify(Film item) throws NotFoundException, InvalidDataException {
		if(item == null)
			throw new InvalidDataException("No puede ser nulo");
		if(item.isInvalid())
			throw new InvalidDataException(item.getErrorsMessage(), item.getErrorsFields());
		var leido = dao.findById(item.getId()).orElseThrow(() -> new NotFoundException());
		return dao.save(item.merge(leido));
	}

	@Override
	public void delete(Film item) throws InvalidDataException {
		if(item == null)
			throw new InvalidDataException("No puede ser nulo");
		deleteById(item.getId());
	}

	@Override
	public void deleteById(Integer id) {
		dao.deleteById(id);
	}

	@Override
	public List<Film> novedades(@NonNull LocalDateTime fecha) {
		return dao.findByLastUpdateGreaterThanEqualOrderByLastUpdate(fecha);
	}

	@Override
	public int actualizaPrecios(short año, double factor) {
		var afectados = dao.findAll((root, query, builder) -> builder.equal(root.get("releaseYear"), año));
		afectados.forEach(item -> item.cambiaPrecios(factor));
		return dao.saveAll(afectados).size();
	}

}
