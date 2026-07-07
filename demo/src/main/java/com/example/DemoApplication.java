package com.example;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.example.base.DummyJSpecify;
import com.example.ioc.GenericoEvent;
import com.example.ioc.NotificationService;
import com.example.ioc.Rango;
import com.example.ioc.anotaciones.EMail;
import com.example.ioc.contratos.ServicioCadenas;
import com.example.ioc.notificaciones.Sender;

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
	
	@Autowired
	NotificationService notify;
	
	@Bean
	CommandLineRunner ioc(ServicioCadenas srv, GenericoEvent ev, @Value("${mi.nombre:Mundo}") String nombre, Rango rango) {
		return arg -> {
			notify.add("Hola %s".formatted(nombre));
			notify.add(rango.toString());
//			ServicioCadenas srv = new ServicioCadenasImpl(new RepositorioCadenasImpl(new ConfiguracionImpl(notify), notify), notify);
			srv.add("añado algo");
			srv.get().forEach(notify::add);
			IO.println("==============>");
			notify.getListado().forEach(IO::println);
			notify.clear();
			IO.println("<==============");
			
		};
	}
	
//	@Bean
	CommandLineRunner porNombre(@EMail Sender sender, List<Sender> listado) {
		return arg -> {
//			sender.send("Envio notificación");
			listado.forEach(o -> o.send("notifica"));
		};
	}

//	@Bean
	CommandLineRunner configuracionEnXML() {
		return _ -> {
			try (var contexto = new FileSystemXmlApplicationContext("applicationContext.xml")) {
				var notify = contexto.getBean(NotificationService.class);
				System.out.println("configuracionEnXML ===================>");
				var srv = (ServicioCadenas) contexto.getBean("servicioCadenas");
				System.out.println(srv.getClass().getName());
				contexto.getBean(NotificationService.class).getListado().forEach(System.out::println);
				System.out.println("===================>");
				srv.get().forEach(notify::add);
				srv.add("Hola mundo");
				notify.add(srv.get(1));
				srv.modify("modificado");
				System.out.println("===================>");
				notify.getListado().forEach(System.out::println);
				notify.clear();
				System.out.println("<===================");
				((Sender) contexto.getBean("sender")).send("Hola mundo");
			}
		};
	}

	@EventListener
	private void eventHandler(GenericoEvent ev) {
		IO.println("Evento generico: %s -> %s".formatted(ev.origen(), ev.carga()));
	}

	@EventListener
	private void eventStringHandler(String ev) {
		IO.println("Tramiento Evento cadena: %s".formatted(ev));
	}
}
