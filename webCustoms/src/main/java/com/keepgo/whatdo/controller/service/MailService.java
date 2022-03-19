package com.keepgo.whatdo.controller.service;

import java.util.Properties;

import javax.mail.Session;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.keepgo.whatdo.ApplicationConfig;
import com.keepgo.whatdo.entity.MailTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MailService {

	private final ApplicationConfig _applicationConfig;
    public MailTO sendMail(MailTO mail) {
    	
    	JavaMailSenderImpl impl = new JavaMailSenderImpl();
    	impl.setPort(Integer.valueOf(_applicationConfig.getEmailPort()));
    	impl.setPassword(_applicationConfig.getEmailPassword());
    	impl.setUsername(_applicationConfig.getEmailEmailAddress());
    	impl.setHost(_applicationConfig.getEmailHost());
    	
    	Properties props = new Properties();
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth" , "true");

    	Session mailSession = Session.getInstance(props, null);
    	impl.setSession(mailSession);
    	impl.setJavaMailProperties(props);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getAddress());
        message.setSubject(mail.getTitle());
        message.setText(mail.getMessage());
        
        message.setFrom(_applicationConfig.getEmailEmailAddress());
        impl.send(message);
        return mail;
    }
}
