package com.keepgo.whatdo.secuirty.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;
import com.keepgo.whatdo.exception.ErrorCode;
import com.keepgo.whatdo.exception.UsernameFromTokenException;
import com.keepgo.whatdo.viewEntity.ErrorResponse;

//@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    public void setErrorResponse(HttpStatus status, HttpServletResponse response,Throwable ex){
    	response.setStatus(status.value());
        response.setContentType("application/json");
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
        errorResponse.setMessage(ex.getMessage());
        try{
//            String json = errorResponse.convertToJson();
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(errorResponse));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		  try{
	          filterChain.doFilter(request,response);
	      } catch (UsernameFromTokenException ex){
	          setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,response,ex);
	      }catch (RuntimeException ex){
	          setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,response,ex);
	      }
		 
	}
}