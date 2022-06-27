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
		ErrorVO exErrorVO = (ErrorVO)request.getAttribute("exception");
//		String exception = (String) request.getAttribute("exception");
		setResponse(response, exErrorVO);
		
	}

	/**
	 * 한글 출력을 위해 getWriter() 사용
	 */
	private void setResponse(HttpServletResponse response, ErrorVO exErrorVO) throws IOException {

		Gson gson = new Gson();
		if (exErrorVO.getErrorCode().equals("403_EXPIREDJWT")) {
			response.setContentType(org.springframework.http.MediaType.APPLICATION_XML.toString());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println(gson.toJson(exErrorVO));
		}

		if (exErrorVO.getErrorCode().equals("403_PASSWORD_INCORRECT")) {
			response.setContentType(org.springframework.http.MediaType.APPLICATION_XML.toString());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println(gson.toJson(exErrorVO));
		}
		
		if (exErrorVO.getErrorCode().equals("403_ID_EMPTY")) {
			response.setContentType(org.springframework.http.MediaType.APPLICATION_XML.toString());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println(gson.toJson(exErrorVO));
		}


	}
}