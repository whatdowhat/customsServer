package com.keepgo.whatdo;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "app")
@PropertySource(encoding = "UTF-8", value = { "classpath:application.properties" })
@EnableTransactionManagement
public class ApplicationConfig {
	
	private String userData01;
	private String userData02;
	private String jpaDriveClassName;
	private String jpaUrl;
	private String jpaU;
	private String jpaP;
	private String devFront;
	private String uploadDir;
	private String administrator;
	private String administratorPassword;
	private String passwordPattern;
	
	private String emailPort;
	private String emailPassword;
	private String emailEmailAddress;
	private String emailHost;
	
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
	
	
	@Bean
	public PropertiesFactoryBean appconfig() {
		PropertiesFactoryBean bean = new PropertiesFactoryBean();
		ClassPathResource classPathResource = new ClassPathResource("application.properties");
		
		bean.setLocation(classPathResource);
		bean.setFileEncoding(StandardCharsets.UTF_8.toString());
		
		return bean;
	}
	
}
