package com.example;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootApplication
public class DemoRestClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoRestClientApplication.class, args);
	}
	
	@Bean
	ActoresProxy proxy(RestClient.Builder builder) {
		RestClient restClient = builder.baseUrl("http://localhost:8010").build();
		RestClientAdapter adapter = RestClientAdapter.create(restClient);
		var factory = HttpServiceProxyFactory.builderFor(adapter).build();
		return factory.createClient(ActoresProxy.class);
	}
	
	@Bean
	CommandLineRunner ejemplo(ActoresProxy proxy) {
		return arg -> {
//			var client = RestClient.create();
//			client.get()
//				.uri("http://localhost:8010/actores/v1?modo=corto")
//				.accept(MediaType.APPLICATION_JSON)
//				.retrieve()
//				.body(new ParameterizedTypeReference<List<Actor>>() {})
//				.forEach(IO::println);
//			proxy.getAll("corto").forEach(IO::println);
			proxy.getAll().forEach(IO::println);
				
		};
	}

}
