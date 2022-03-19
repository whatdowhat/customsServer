package com.keepgo.whatdo.secuirty.handler;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler{

	static final Logger log = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);
	private static Logger LOGGER = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);
	private final String DEFAULT_FAILURE_URL = "/login.do?error=true";
	
	private String redirectUrl;
	
	public CustomAccessDeniedHandler() {
		
		
	}
	
	public CustomAccessDeniedHandler(String redirectUrl) {
		
		this.redirectUrl = redirectUrl;
		log.debug("enter ===CustomAccessDeniedHandlerhandleer==============>{}","CustomAccessDeniedHandler");
		
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// TODO Auto-generated method stub
		log.debug("enter ===CustomAccessDeniedHandlerhandleer==============>{}","CustomAccessDeniedHandler");
		System.out.println("FFFFFFFFFFFFFFF CustomAccessDeniedHandler");
		log.debug("handle test");
		String referer = (String)request.getHeader("REFERER");
		log.debug(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort());
		log.debug("referer test"+referer);
		try {
			referer = referer.replace(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort(), "");	
		} catch (Exception e) {
			String errorMessage = "권한이 없는 접근입니다. 관리자에게 문의하세요.";
			request.setAttribute("errorMessage", errorMessage);
			RequestDispatcher rd;
			rd = request.getRequestDispatcher("/");
			rd.forward(request, response); 
		}
		
		log.debug("referer test"+referer);
		
		
		log.debug(referer);
		String errorMessage = "권한이 없는 접근입니다. 관리자에게 문의하세요.";
		request.setAttribute("errorMessage", errorMessage);
		RequestDispatcher rd;
		if(referer == null || referer.equals("")) {
			rd = request.getRequestDispatcher("/");
		}else {
			rd = request.getRequestDispatcher(referer);	
		}
		rd.forward(request, response); 
		
		
	}

	public void setErrorPage(String string) {
		// TODO Auto-generated method stub
		
	}

	
}
