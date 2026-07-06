package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.base.DummyJSpecify;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.err.println("Aplicacion arrancada ...");
		log.warn("Aplicacion arrancada ...");
	}
	
//	@Bean
	CommandLineRunner nulable() {
		return arg -> {
			try {
				var dummy = new DummyJSpecify("algo");
//				if(dummy.hasCadena())
//					IO.println(dummy.getCadena().toUpperCase());
//				if(dummy.getCadenaSegura().isPresent())
//					IO.println(dummy.getCadenaSegura().get().toUpperCase());
//				if(dummy.getCadenaSegura().isPresent())
					IO.println(dummy.getCadenaSegura().orElse("").toUpperCase());
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}

}
