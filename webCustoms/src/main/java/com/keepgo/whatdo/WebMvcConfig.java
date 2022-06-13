package com.keepgo.whatdo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.BeanNameViewResolver;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
//@ImportResource({"classpath:/dispatcher-servlet.xml"})
//@ImportResource({"/dispatcher-servlet.xml"})
@Configuration
public class WebMvcConfig  implements WebMvcConfigurer {

	private final ApplicationConfig _applicationConfig;
	
	
	
	@Bean
	public CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return characterEncodingFilter;
	}
	
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


    }

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index.jsp");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// TODO Auto-generated method stub
//		WebMvcConfigurer.super.addCorsMappings(registry);
		registry.addMapping("/**").allowedOrigins("http://localhost:3000");
        registry.addMapping("/**")
        //           
        .allowedOrigins("*")
        //             
        .allowCredentials(true)
        //       
        .allowedMethods("*")
        .allowedHeaders("*")
        //      
        .maxAge(3600);
		 
		  
		  
	}
	
	@Bean
	public BeanNameViewResolver beanNameViewResolver() {
		BeanNameViewResolver excelView = new BeanNameViewResolver();
		excelView.setOrder(2);
		 return excelView;
	}
	
	
    
    
}
