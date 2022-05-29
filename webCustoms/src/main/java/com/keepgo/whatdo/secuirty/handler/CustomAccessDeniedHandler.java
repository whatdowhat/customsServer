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
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.keepgo.whatdo.entity.ErrorVO;
import com.keepgo.whatdo.exception.UsernameFromTokenException;

@Component
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
		request.setAttribute("exception", ErrorVO.builder().status(200).errorCode("NO_AUTHORITIES").errorMessage("NO_AUTHORITIES").build());
		throw new UsernameFromTokenException("Username from token error");
		
		
	}

	public void setErrorPage(String string) {
		// TODO Auto-generated method stub
		
	}

	
	private void setResponse(HttpServletResponse response, String errorCode) throws IOException {

		Gson gson = new Gson();
		if (errorCode.equals("403_EXPIREDJWT")) {
			response.setContentType(org.springframework.http.MediaType.APPLICATION_XML.toString());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			String resultObj = gson.toJson(
					ErrorVO.builder().status(200).errorCode(errorCode).errorMessage("JWT Token has expired").build());

			response.getWriter().println(resultObj);
		}

		if (errorCode.equals("403_PASSWORD_INCORRECT")) {
			response.setContentType(org.springframework.http.MediaType.APPLICATION_XML.toString());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			String resultObj = gson.toJson(
					ErrorVO.builder().status(200).errorCode(errorCode).errorMessage("PASSWORD INCORRECT").build());
			response.getWriter().println(resultObj);
		}
		
		if (errorCode.equals("403_ID_EMPTY")) {
			response.setContentType(org.springframework.http.MediaType.APPLICATION_XML.toString());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			String resultObj = gson.toJson(
					ErrorVO.builder().status(200).errorCode(errorCode).errorMessage("ID EMPTY").build());
			response.getWriter().println(resultObj);
		}


	}
	
}
