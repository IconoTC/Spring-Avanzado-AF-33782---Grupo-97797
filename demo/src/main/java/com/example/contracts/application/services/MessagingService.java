package com.example.contracts.application.services;

public interface MessagingService {

	void sendWelcomeEmail(String to, String name);

	void sendWelcomeEmailAsync(String to, String name);

	void sendEmail(String to, String subject, String body);

	void sendEmailAsync(String to, String subject, String body);

	void sendEmail(String to, String subject, String body, String attachmentPath);

	void sendEmailAsync(String to, String subject, String body, String attachmentPath);

	void sendMimeEmail(String to, String subject, String htmlBody, boolean isHtml);

	void sendMimeEmailAsync(String to, String subject, String htmlBody, boolean isHtml);

}