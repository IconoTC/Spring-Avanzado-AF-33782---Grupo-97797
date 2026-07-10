package com.example;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "/actores/v1", accept = "application/json", contentType =  "application/json")
public interface ActoresProxy {
	@GetExchange
	List<Actor> getAll(@RequestParam() String modo);
	@GetExchange
	List<Actor> getAll();
}
