package com.example;

import java.util.List;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jms.autoconfigure.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.JacksonJsonMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Microservicio: JMS Receptor", 
				version = "1.0", 
				description = "Ejemplo de receptor JMS con ActiveMQ.", 
				license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html"), 
				contact = @Contact(name = "Javier Martín", url = "https://github.com/jmagit", email = "support@example.com")), 
		externalDocs = @ExternalDocumentation(description = "Documentación del proyecto", url = "https://github.com/jmagit/REM20220725"))
public class AsyncJmsReceptorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsyncJmsReceptorApplication.class, args);
	}

    @Bean
    ActiveMQConnectionFactory connectionFactory(@Value("${spring.activemq.broker-url}") String brokerUrl, 
    		@Value("${spring.activemq.user}") String userName,@Value("${spring.activemq.password}") String password) {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(userName, password, brokerUrl);
        factory.setTrustedPackages(List.of("com.example")); // Restricts deserialization to trusted packages
        return factory;
    }

	@Bean
	JmsListenerContainerFactory<?> topicPubSubFactory(ActiveMQConnectionFactory connectionFactory, 
			DefaultJmsListenerContainerFactoryConfigurer configurer,
			@Value("${client-id}") String clientId) {
	    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory); // Copia los valores por defecto predeterminados
		factory.setClientId(clientId); // Establece un ClientID único para la aplicación nesesario si es 
		factory.setPubSubDomain(true); // Activa el modo eventos (topic)
	    factory.setSubscriptionDurable(true); // Activa el modo de suscripción duradera (requiere ClientID)
	    
	    return factory;
	}

	@Bean // Serialize message content to json using TextMessage
	MessageConverter jacksonJmsMessageConverter() {
		var converter = new JacksonJsonMessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

}
