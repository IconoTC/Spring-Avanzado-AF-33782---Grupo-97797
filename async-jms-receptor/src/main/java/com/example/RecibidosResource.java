package com.example;

import java.util.List;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RecibidosResource {

	// -Dorg.apache.activemq.SERIALIZABLE_PACKAGES=*
	@JmsListener(destination = "saludos"/* , containerFactory = "myFactory" */)
	public void listenQueue(MessageDTO in) throws InterruptedException {
		Store.addQueue(new Message(in));
		log.warn("MENSAJE RECIBIDO: " + in);
		Thread.sleep(1000);
	}

	@Tag(name = "Queues")
	@GetMapping("/saludos")
	public List<Message> getQueue() {
		return Store.getQueue();
	}
	@Tag(name = "Queues")
	@DeleteMapping("/saludos")
	public List<Message> clearQueue() {
		Store.clearQueue();
		return Store.getQueue();
	}

	@JmsListener(destination = "despedidas", containerFactory = "topicPubSubFactory", subscription = "${subscription-name}")
	public void listen(MessageDTO in) {
		Store.addTopic(new Message(in));
		log.warn("TEMA RECIBIDO: " + in);
	}

	@Tag(name = "Topics")
	@GetMapping("/despedidas")
	public List<Message> getTopic() {
		return Store.getTopic();
	}
	@Tag(name = "Topics")
	@DeleteMapping("/despedidas")
	public List<Message> clearTopic() {
		Store.clearTopic();
		return Store.getTopic();
	}

}
