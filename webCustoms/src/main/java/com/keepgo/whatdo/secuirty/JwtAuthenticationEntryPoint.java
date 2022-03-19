package com.keepgo.whatdo.secuirty;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.keepgo.whatdo.entity.ErrorVO;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = -7858869558953243875L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		System.out.println(request.getRequestURI());
		String exception = (String) request.getAttribute("exception");

		System.out.println("######################### JwtAuthenticationEntryPoint");
		System.out.println("######################### JwtAuthenticationEntryPoint : " + exception);
		if (exception.equals("403_EXPIREDJWT")) {
			setResponse(response, "403_EXPIREDJWT");
		}else
		if (exception.equals("403_PASSWORD_INCORRECT")) {
			setResponse(response, "403_PASSWORD_INCORRECT");
		}else
		if (exception.equals("403_ID_EMPTY")) {
			setResponse(response, "403_ID_EMPTY");
		}else {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");	
		}
		
	}

	/**
	 * 한글 출력을 위해 getWriter() 사용
	 */
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