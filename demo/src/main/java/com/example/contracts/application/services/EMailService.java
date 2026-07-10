package com.example.contracts.application.services;

import org.springframework.scheduling.annotation.Async;

public interface EMailService {
	/**
	 * Envia un correo electrónico.
	 *
	 * @param to destinatario del correo
	 * @param subject asunto del correo
	 * @param body cuerpo del correo
	 */
	void sendEmail(String to, String subject, String body);

	/**
	 * Envia asincronamente un correo electrónico.
	 *
	 * @param to destinatario del correo
	 * @param subject asunto del correo
	 * @param body cuerpo del correo
	 */
	@Async
	void sendEmailAsync(String to, String subject, String body);

	/**
	 * Envia un correo electrónico con un archivo adjunto.
	 *
	 * @param to destinatario del correo
	 * @param subject asunto del correo
	 * @param body cuerpo del correo
	 * @param attachmentPath ruta del archivo adjunto
	 */
	void sendEmailWithAttachment(String to, String subject, String body, String attachmentPath);

	/**
	 * Envia asincronamente un correo electrónico con un archivo adjunto.
	 *
	 * @param to destinatario del correo
	 * @param subject asunto del correo
	 * @param body cuerpo del correo
	 * @param attachmentPath ruta del archivo adjunto
	 */
	@Async
	void sendEmailWithAttachmentAsync(String to, String subject, String body, String attachmentPath);
	
	/**
	 * Envia un correo electrónico MIME, que puede incluir HTML y archivos adjuntos.
	 *
	 * @param to destinatario del correo
	 * @param subject asunto del correo
	 * @param htmlBody cuerpo del correo en formato HTML
	 * @param isHtml indica si el cuerpo es HTML o texto plano
	 */
	void sendMimeEmail(String to, String subject, String htmlBody, boolean isHtml);
	
	/**
	 * Envia asincronamente un correo electrónico MIME, que puede incluir HTML y archivos adjuntos.
	 *
	 * @param to destinatario del correo
	 * @param subject asunto del correo
	 * @param htmlBody cuerpo del correo en formato HTML
	 * @param isHtml indica si el cuerpo es HTML o texto plano
	 */
	@Async
	void sendMimeEmailAsync(String to, String subject, String htmlBody, boolean isHtml);
}
