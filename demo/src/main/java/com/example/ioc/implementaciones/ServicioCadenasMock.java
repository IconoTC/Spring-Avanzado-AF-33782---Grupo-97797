package com.example.ioc.implementaciones;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ioc.NotificationService;
import com.example.ioc.anotaciones.Pruebas;
//import com.example.ioc.anotaciones.Pruebas;
import com.example.ioc.contratos.RepositorioCadenas;
import com.example.ioc.contratos.ServicioCadenas;

//@Service
//@Profile("test")
//@Pruebas
public class ServicioCadenasMock implements ServicioCadenas {
	private final RepositorioCadenas dao;
	private final NotificationService notify;

	public ServicioCadenasMock(RepositorioCadenas dao, NotificationService notify) {
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
	public void add(String item) {
		dao.save(item);
	}

	@Override
	public void modify(String item) {
		notify.add("Simulo que modifico los datos sin llamar al repositorio.");
	}

	@Override
	public void remove(Integer id) {
		notify.add("Simulo que borro los datos sin llamar al repositorio.");
	}

}
