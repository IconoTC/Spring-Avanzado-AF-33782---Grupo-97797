package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.base.DummyJSpecify;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.err.println("Aplicacion arrancada ...");
	}
	
	@Bean
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
