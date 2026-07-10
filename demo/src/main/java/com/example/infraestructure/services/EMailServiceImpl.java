package com.example.infraestructure.services;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.contracts.application.services.EMailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@Primary
public class EMailServiceImpl implements EMailService {
	Logger log = LoggerFactory.getLogger(EMailServiceImpl.class);

	@Value("${spring.mail.username:adm@example.com}")
	private String from;
	@Autowired
	private JavaMailSender mailSender; // Spring inyectará automáticamente la implementación configurada

	@Override
	public void sendEmail(String to, String subject, String body) {
		log.info("🚀 [Hilo: %s] Iniciando envío de correo a: %s".formatted(Thread.currentThread().getName(), to));
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from); // Reemplaza con tu remitente configurado
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
		log.info("✅ [Hilo: %s] Correo enviado a %s (%s)".formatted(Thread.currentThread().getName(), to, subject));
	}

	@Async
	@Override
	public void sendEmailAsync(String to, String subject, String body) {
		sendEmail(to, subject, body);
		
	}

	@Override
	public void sendEmailWithAttachment(String to, String subject, String body, String attachmentPath) {
		log.info("🚀 [Hilo: %s] Iniciando envío de correo a: %s".formatted(Thread.currentThread().getName(), to));
		try {
			FileSystemResource file = new FileSystemResource(new File(attachmentPath));
			MimeMessage message = mailSender.createMimeMessage();
			// true = multipart message, false = single part message
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body); // true = HTML, false = plain text
			helper.addAttachment(file.getFilename(), file);
			mailSender.send(message);
			log.info("Correo MIME (HTML/Adjuntos) enviado a: " + to);
		} catch (MessagingException e) {
			throw new RuntimeException("Error al enviar el correo con adjunto", e);
		}
		log.info("✅ [Hilo: %s] Correo enviado a %s (%s)".formatted(Thread.currentThread().getName(), to, subject));
	}

	@Async
	@Override
	public void sendEmailWithAttachmentAsync(String to, String subject, String body, String attachmentPath) {
		sendEmailWithAttachment(to, subject, body, attachmentPath);
		
	}

	@Async
	@Override
	public void sendMimeEmail(String to, String subject, String htmlBody, boolean isHtml) {
		log.info("🚀 [Hilo: %s] Iniciando envío de correo a: %s".formatted(Thread.currentThread().getName(), to));
		try {
//			MimeMessage message = mailSender.createMimeMessage();
//			// true = multipart message, false = single part message			
//			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//			helper.setFrom(from);
//			helper.setTo(to);
//			helper.setSubject(subject);
//			helper.setText(htmlBody, isHtml); // true = HTML, false = plain text
//			mailSender.send(message);
			mailSender.send(message -> {
				MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
				helper.setFrom(from);
				helper.setTo(to);
				helper.setSubject(subject);
				helper.setText(htmlBody, isHtml); // true = HTML, false = plain text
			});
			log.info("Correo MIME (HTML/Adjuntos) enviado a: " + to);
		} catch (Exception e) {
			throw new RuntimeException("Error al enviar el correo MIME", e);
		}
		log.info("✅ [Hilo: %s] Correo MIME (HTML/Adjuntos) enviado a %s (%s)".formatted(Thread.currentThread().getName(), to, subject));
	}
	
	@Async
	@Override
	public void sendMimeEmailAsync(String to, String subject, String htmlBody, boolean isHtml) {
		sendMimeEmail(to, subject, htmlBody, isHtml);
	}

}
