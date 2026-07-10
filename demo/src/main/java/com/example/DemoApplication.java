package com.example;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.aop.AuthenticationService;
import com.example.aop.introductions.Visible;
import com.example.aop.introductions.VisibleAspect;
import com.example.base.DummyAsync;
import com.example.base.DummyJSpecify;
import com.example.base.DummyRetry;
import com.example.contracts.application.services.MessagingService;
import com.example.ioc.GenericoEvent;
import com.example.ioc.NotificationService;
import com.example.ioc.Rango;
import com.example.ioc.anotaciones.EMail;
import com.example.ioc.contratos.ServicioCadenas;
import com.example.ioc.implementaciones.ConfiguracionImpl;
import com.example.ioc.implementaciones.RepositorioCadenasImpl;
import com.example.ioc.implementaciones.ServicioCadenasImpl;
import com.example.ioc.notificaciones.Sender;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableResilientMethods
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

//	@Bean
	CommandLineRunner ioc(ServicioCadenas srv, AuthenticationService auth, /* GenericoEvent ev, */
			@Value("${mi.nombre:Mundo}") String nombre, Rango rango, DummyJSpecify dummy) {
		return arg -> {
			try {
				notify.add("Hola %s".formatted(nombre));
				notify.add(rango.toString());
//			ServicioCadenas srv = new ServicioCadenasImpl(new RepositorioCadenasImpl(new ConfiguracionImpl(notify), notify), notify);
				auth.login();
//				System.err.println(srv.getClass().getName());
				srv.add("añado algo");
				srv.get().forEach(notify::add);
//				IO.println("==============>");
//				notify.getListado().forEach(IO::println);
//				notify.clear();
//				IO.println("<==============");
//				dummy.setCadenaSegura(null);
			} catch (Exception e) {
				e.printStackTrace();
			}

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

//	@EventListener
//	private void eventHandler(GenericoEvent ev) {
//		IO.println("Evento generico: %s -> %s".formatted(ev.origen(), ev.carga()));
//	}
//
//	@EventListener
//	private void eventStringHandler(String ev) {
//		IO.println("Tramiento Evento cadena: %s".formatted(ev));
//	}

//	@Bean
	CommandLineRunner introducciones(@EMail Sender sender) {
		return arg -> {
			if (sender instanceof Visible v) {
				IO.println(v.isVisible() ? "es visible" : "NO es visible");
				v.mostrar();
				IO.println(v.isVisible() ? "es visible" : "NO es visible");
				v.ocultar();
				IO.println(v.isVisible() ? "es visible" : "NO es visible");
			} else {
				IO.println("NO implementa el interfaz Visible");
			}
		};
	}

//	@Bean
	CommandLineRunner generarProxyAOPManualmente() {
		return arg -> {
			try {
				var dummy = new Rango();
				AspectJProxyFactory factory = new AspectJProxyFactory(dummy);
				factory.addAspect(VisibleAspect.class);
				dummy = factory.getProxy();
				if (dummy instanceof Visible v) {
					IO.println(v.isVisible() ? "es visible" : "NO es visible");
					v.mostrar();
					IO.println(v.isVisible() ? "es visible" : "NO es visible");
					v.ocultar();
					IO.println(v.isVisible() ? "es visible" : "NO es visible");
				} else {
					IO.println("Rango NO implementa el interfaz Visible");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}

	@Scheduled(timeUnit = TimeUnit.SECONDS, fixedRate = 5, initialDelay = 5)
	void mostrarNotificaciones() {
		// IO.println("Han pasado 5 segundos");
		if (notify.hasMessages()) {
			IO.println("==============>");
			notify.getListado().forEach(IO::println);
			notify.clear();
			IO.println("<==============");
		}
	}

//	@Bean
	CommandLineRunner asincrono(DummyAsync dummy) {
		return arg -> {
			var obj = dummy; // new DummyAsync();
			System.err.println(obj.getClass().getCanonicalName());
			obj.ejecutarAutoInvocado(1);
			obj.ejecutarAutoInvocado(2);
//			obj.ejecutarTareaSimpleAsync(1);
//			obj.ejecutarTareaSimpleAsync(2);
//			obj.calcularResultadoAsync(10, 20, 30, 40, 50).thenAccept(result -> notify.add(result));
//			obj.calcularResultadoAsync(1, 2, 3).thenAccept(result -> notify.add(result));
//			obj.calcularResultadoAsync().thenAccept(result -> notify.add(result));
			System.err.println("Termino de mandar hacer las cosas");
		};
	}

//	@Bean
	CommandLineRunner resiliencia(DummyRetry dummy) {
		return arg -> {
			try {
				IO.println("------------------> reintentaConAnotacion: " + dummy.reintentaConAnotacion(3));
				IO.println("------------------> reintentaConAnotacion: " + dummy.reintentaConAnotacion(5));
			} catch (Exception e) {
				System.err.println("ERROR reintentaConAnotacion: " + e.getMessage());
			}
			dummy.reinicia();
			try {
				IO.println("------------------> reintentaConTemplate: " + dummy.reintentaConTemplate(3));
				IO.println("------------------> reintentaConTemplate: " + dummy.reintentaConTemplate(5));
			} catch (Exception e) {
				System.err.println("ERROR reintentaConTemplate: " + e.getMessage());
			}
		};
	}

//	@Bean
	CommandLineRunner limites(DummyRetry dummy, DummyAsync obj) {
		return arg -> {
			for (var i = 1; ++i <= 10;) {
				obj.calcularResultadoAsync(10 * i, 20 * i, 30 * i, 40 * i, 50 * i).exceptionally(e -> {
					return "ERROR %s".formatted(e.getMessage());
				}).thenAccept(result -> notify.add(result));
			}
		};
	}
	
	@Bean
	CommandLineRunner demosCorreos(MessagingService mensajeria) {
		return _ -> {
			mensajeria.sendWelcomeEmailAsync("pgrillo@example.com", "Pepito Grillo");
//			mensajeria.sendEmailAsync("admin@example.com", "Aplicacion Init", "La aplicacion se ha iniciado");
			var body = """
					<!DOCTYPE html>
					<html lang="es">
					<head>
					    <meta charset="UTF-8">
					    <meta name="viewport" content="width=device-width, initial-scale=1.0">
					    <title>Servicio</title>
					</head>
					<body>
					    <h1>%s</h1>
					    <p>%s</p>
					</body>
					</html>
					""".formatted("Aplicacion Open", "La aplicacion se ha iniciado");
			mensajeria.sendMimeEmailAsync("admin@example.com", "Aplicacion Open", body, true);
		};
	}

}
