package com.keepgo.whatdo.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Controller
@CrossOrigin
public class FrontController implements ErrorController {
//public class FrontController  {

//	 @RequestMapping(value = "/error")
//	    public ResponseEntity<Object> handleNoHandlerFoundException(HttpServletResponse response, HttpServletRequest request) {
//	        return new ResponseEntity<>(request.getAttribute("exception"), HttpStatus.INTERNAL_SERVER_ERROR);
//	    }
	 @RequestMapping(value = "/error")
	    public String  handleNoHandlerFoundException(HttpServletResponse response, HttpServletRequest request) {
		 
//		 int status = response.getStatus();
//		 System.out.println(status);
//	        WebRequest webRequest = new ServletWebRequest(request);
//	        Map<String, String[]> errorRequest =webRequest.getParameterMap();
//	        System.out.println(errorRequest.keySet().toString());

//	        int errorStatus = (int) errorRequest.get("status");
//	        String errorMessage =  errorRequest.get("error").toString();
 	        return "/index.html";
	    }
	@Override
	public String getErrorPath() {
		return "/error";
	}
	
//	
	
	

}
