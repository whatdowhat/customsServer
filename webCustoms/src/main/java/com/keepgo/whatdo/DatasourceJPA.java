package com.keepgo.whatdo;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatasourceJPA {
	
//	private final ApplicationConfig applicationConfig;
	
	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		HikariConfig hikariConfig = new HikariConfig();
	
//		  hikariConfig.setDriverClassName(applicationConfig.getJpaDriveClassName());
//		  hikariConfig .setJdbcUrl(applicationConfig.getJpaUrl()); 
//		  hikariConfig.setUsername(applicationConfig.getJpaU());
//		  hikariConfig.setPassword(applicationConfig.getJpaP());

		
//		app.jpaDriveClassName=
//				app.jpaUrl=
//				app.jpaU=peacherp
//				app.jPaP=vlcldldkfvl!
		
		  
//		  
		  
//		  								   com.mysql.cj.jdbc.Driver
//		  hikariConfig .setJdbcUrl("jdbc:mysql://peacherp.cafe24.com/peacherp?useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Seoul"); 
//		  hikariConfig.setUsername("peacherp");
//		  hikariConfig.setPassword("vlcldldkfvl!");
		  
		hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
		  hikariConfig.setJdbcUrl("jdbc:mysql://keepgo.cafe24app.com:3306/keepgoing01?useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Seoul");
		  hikariConfig.setUsername("keepgoing01");
		  hikariConfig.setPassword("alsrh132@");
		  
//		hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
//		  hikariConfig .setJdbcUrl("jdbc:mysql://localhost/adminweb?useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Seoul"); 
//		  hikariConfig.setUsername("admin");
//		  hikariConfig.setPassword("root");
		  
		  
		HikariDataSource dataSource = new HikariDataSource(hikariConfig);
		

		return dataSource;
	}


	@Bean
	public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
		return entityManagerFactory.createEntityManager();
	}

	@Bean
	public FactoryBean<EntityManagerFactory> entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean containerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		containerEntityManagerFactoryBean.setDataSource(dataSource());
		JpaVendorAdapter adaptor = new HibernateJpaVendorAdapter();
		containerEntityManagerFactoryBean.setJpaVendorAdapter(adaptor);
//		containerEntityManagerFactoryBean.setPackagesToScan("com.whatdo.keep.config.* com.whatdo.keep.repository.* com.whatdo.keep.vo.*");
		containerEntityManagerFactoryBean.setPackagesToScan("com.keepgo.whatdo.*");
		Properties props = new Properties();
		
		props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		
		
//		dev
		props.setProperty("hibernate.SQL", "info");
		props.setProperty("hibernate.show_sql", "true");

//		real
//		props.setProperty("hibernate.SQL", "info");
//		props.setProperty("hibernate.show_sql", "true");
		props.setProperty("hibernate.format_sql", "true");
		props.setProperty("hibernate.use_sql_comments", "true");
		props.setProperty("spring.jpa.properties.hibernate.format_sql", "true");
		
		props.setProperty("hibernate.hbm2ddl.auto", "update");
		

		
		props.setProperty("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation", "true");
				
		containerEntityManagerFactoryBean.setJpaProperties(props);
		return containerEntityManagerFactoryBean;
	}

	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
		return jpaTransactionManager;
	}
	

}
