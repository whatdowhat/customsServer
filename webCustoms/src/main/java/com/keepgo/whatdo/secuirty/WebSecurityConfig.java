package com.keepgo.whatdo.secuirty;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.keepgo.whatdo.secuirty.filter.JwtRequestFilter;
import com.keepgo.whatdo.secuirty.handler.CustomAccessDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	final String SUPER ="SUPER";
	final String ADMIN ="ADMIN";
	final String INBOUND ="INBOUND";
	final String USER ="USER";
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

//    @Autowired
//    private ExceptionHandlerFilter exceptionHandlerFilter;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	
	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;

    
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		web.ignoring().antMatchers("/resources/**").antMatchers(HttpMethod.OPTIONS);
	}

	
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setExposedHeaders(Arrays.asList("custom-header"));
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	 
	
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// For CORS error

//    	CorsConfiguration corsConfig = new CorsConfiguration();
//    	corsConfig.setAllowedMethods(Arrays.asList("*"));
//    	corsConfig.setAllowedOrigins(Arrays.asList("*"));
//    	httpSecurity.authorizeRequests()
//           .antMatchers("/resources/**").permitAll(); // no effect
		httpSecurity.cors();
//		CorsConfiguration corsConfig = new CorsConfiguration().applyPermitDefaultValues();
//		corsConfig.setExposedHeaders(Arrays.asList("custom-header")); // exceldownload 시 file name header
//		corsConfig.addAllowedOrigin("*");
//		corsConfig.addAllowedHeader("*");
//		corsConfig.addAllowedMethod("*");
//		corsConfig.setAllowCredentials(true);
//		
//        httpSecurity.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
//		httpSecurity.cors().configurationSource(request -> corsConfig);
		// We don't need CSRF for this example
		httpSecurity.csrf().disable()
		// dont authenticate this particular request
//        	.authorizeRequests().antMatchers("/**").permitAll().and()
//        	

//            .authorizeRequests().antMatchers("/authenticate/user").hasAuthority("ADMIN")
//            .and()

            .authorizeRequests().antMatchers("/test/**").permitAll().and()
            .authorizeRequests().antMatchers("/test/**/**").permitAll().and()
				.authorizeRequests()
				.antMatchers("/", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html",
						"/**/*.css", "/**/*.js","/pub/**")
				.permitAll()
				
				//권한설정 url mapping
//				.antMatchers("/front/getAllCondition").hasAnyAuthority(SUPER,ADMIN)
//				.antMatchers("/front/getAllCondition").hasAnyAuthority(ADMIN)
				//권한설정 url mapping
				
				
				.antMatchers("/index.jsp").permitAll()
				.antMatchers("/static/index.html").permitAll()
				.antMatchers("/excel/**").permitAll()
				.antMatchers("/migration/**").permitAll()
				.antMatchers("/company/**").permitAll()
				.antMatchers("/common/**").permitAll()
				.antMatchers("/user/**").permitAll()
				.antMatchers("/inbound/**").permitAll()
				
				.antMatchers("/front/**").permitAll()
				.antMatchers("/front/**/**").permitAll()
				.antMatchers("/authenticate").permitAll()
				
				.antMatchers("/otherEroor").permitAll().antMatchers("/error").permitAll().antMatchers("/index.html")
				.permitAll()
//            .authorizeRequests().antMatchers("/authenticate/user").hasAuthority("ADMIN").and().
				
				// all other requests need to be authenticated
				.anyRequest().authenticated().and().
				exceptionHandling().accessDeniedHandler(accessDeniedHandler).and().
				exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.accessDeniedHandler(new CustomAccessDeniedHandler()).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//             stateless session exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
//             sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
//		JwtRequestFilter jwtRequestFilter = new JwtRequestFilter();
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//		httpSecurity.addFilterBefore(exceptionHandlerFilter, JwtRequestFilter.class);
	}
}