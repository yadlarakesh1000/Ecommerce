package com.ecommerce.service;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
	private final JavaMailSender javaMailSender;
      @Async
      @Retryable(
    		retryFor =MailException.class,
    		maxAttempts =3,
    		backoff= @Backoff(delay=2000)
    		  )
      
	public void sendVerificationOtpEmail(String userEmail, String otp, String subject, String text) throws MessagingException {
		System.out.println("\n========================================================");
		System.out.println("  GENERATED OTP FOR [" + userEmail + "]: " + otp);
		System.out.println("========================================================\n");
		try {
			MimeMessage mimeMessage =javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
					                       mimeMessage,"utf-8"
					);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(text + otp);
			mimeMessageHelper.setTo(userEmail);
			mimeMessageHelper.setFrom("noreply@email.com");
			javaMailSender.send(mimeMessage);
			
			log.info("OTP email sent to {}",userEmail);
			
		}
		catch(MailException e) {
			 
			throw new MailException("FAILED to SEND email") {
			};
		}
		
		
	}
}
