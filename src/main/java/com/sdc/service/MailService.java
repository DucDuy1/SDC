package com.sdc.service;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	@Autowired
	private JavaMailSender javaMailSender;

	public void sendEmail(String to, String subject, String body) {
		try {

			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

			helper.setTo(to);
			helper.setText(body, true);
			helper.setSubject(subject);
			helper.setFrom("doducduy1159@gmail.com");
			javaMailSender.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();
//				    logger.error("Email sent with error: " + e.getMessage());
		}

	}
}
