package com.example.ioc.implementaciones;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.example.ioc.NotificationService;
import com.example.ioc.contratos.Repositorio;
import com.example.ioc.contratos.RepositorioCadenas;
import com.example.ioc.contratos.Servicio;
import com.example.ioc.contratos.ServicioCadenas;

@Service
//@Primary
@Profile({"prod", "default"})
public class ServicioCadenasImpl implements ServicioCadenas {
	private final RepositorioCadenas dao;
	private final NotificationService notify;

	public ServicioCadenasImpl(RepositorioCadenas dao, NotificationService notify) {
		this.dao = dao;
		this.notify = notify;
		notify.add(getClass().getSimpleName() + " Constructor");
	}

	@Override
	public List<String> get() {
		return List.of(dao.load(), dao.load(), dao.load());
	}

	@Override
	public String get(Integer id) {
		return dao.load();
	}

	@Override
//	@LoggerAll
	public void add(String item) {
		if (item == null || item.trim() == "")
			throw new IllegalArgumentException("Datos invalidos.");
		dao.save(item);
	}

	@Override
//	@Logged
	public void modify(String item) {
		if (item == null || item.trim() == "")
			throw new IllegalArgumentException("Datos invalidos.");
		dao.save(item);
	}

	@Override
	public void remove(Integer id) {
		dao.save(id.toString());
	}
	@EventListener
	void eventRepository(String ev) {
		System.err.println("Soy el servicio: %s".formatted(ev));
	}

}
