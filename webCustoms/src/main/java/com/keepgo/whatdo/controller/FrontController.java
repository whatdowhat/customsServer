package com.keepgo.whatdo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin
public class FrontController implements ErrorController {

//	 @RequestMapping(value = "/error")
//	    public ResponseEntity<Object> handleNoHandlerFoundException(HttpServletResponse response, HttpServletRequest request) {
//	        return new ResponseEntity<>(request.getAttribute("exception"), HttpStatus.INTERNAL_SERVER_ERROR);
//	    }
	 @RequestMapping(value = "/error")
	    public String  handleNoHandlerFoundException(HttpServletResponse response, HttpServletRequest request) {
	        return "/index.html";
	    }
	@Override
	public String getErrorPath() {
		return "/error";
	}

}
