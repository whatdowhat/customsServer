package com.keepgo.whatdo.secuirty.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;
import com.keepgo.whatdo.entity.ErrorVO;
import com.keepgo.whatdo.entity.customs.User;
import com.keepgo.whatdo.repository.UserRepository;
import com.keepgo.whatdo.secuirty.JwtTokenUtil;
import com.keepgo.whatdo.secuirty.JwtUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

@Component
@CrossOrigin
public class JwtRequestFilter extends OncePerRequestFilter {

    @Value("${jwt.secretYn}")
    private String secretYn;
    
    @Value("${app.administrator}")
    private String ADMINISTRATOR;
    
	@Autowired
	UserRepository _user;
	
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

//    	chain.doFilter(request, response);
    	 Gson gson = new Gson();
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())||
        		request.getRequestURI().startsWith("/static") ||
        		request.getRequestURI().startsWith("/custom_container.js")||
        		request.getRequestURI().startsWith("/index.css")||
        		request.getRequestURI().startsWith("/manifest.json")||
        		request.getRequestURI().startsWith("/favicon.ico")
        		
        		) {
            chain.doFilter(request, response);
        }else {
        	if(secretYn.equals("false")) {
        		//검증 
            	chain.doFilter(request, response);	
        	}else {
        		//운영
            	
            	final String requestTokenHeader = request.getHeader("Authorization");

                String username = null;
                String jwtToken = null;
                // JWT Token is in the form "Bearer token". Remove Bearer word and get
                // only the Token
                if(request.getRequestURI().equals("/authenticate")
                		||request.getRequestURI().equals("/index.html")
                		||request.getRequestURI().equals("/")){
                	chain.doFilter(request, response);
                }else {
                    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                        
                    	
                        try {
                        	jwtToken = requestTokenHeader.substring(7);
                            username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                        } catch (IllegalArgumentException e) {
                			response.setContentType(org.springframework.http.MediaType.APPLICATION_XML.toString());
                			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                			String resultObj = gson.toJson(
                					ErrorVO.builder().status(200).errorCode("403_EXPIREDJWT").errorMessage("JWT Token has expired").build());
                			response.getWriter().println(resultObj);
                			return;
                        } catch (ExpiredJwtException e) {
//                        	
                			response.setContentType(org.springframework.http.MediaType.APPLICATION_XML.toString());
                			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                			String resultObj = gson.toJson(
                					ErrorVO.builder().status(200).errorCode("403_EXPIREDJWT").errorMessage("JWT Token has expired").build());
                			response.getWriter().println(resultObj);

                			response.getWriter().println(resultObj);
                			 return;
                        	
                        } catch (Exception e) {
                        	
                			response.setContentType(org.springframework.http.MediaType.APPLICATION_XML.toString());
                			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                			String resultObj = gson.toJson(
                					ErrorVO.builder().status(200).errorCode("403_PASSWORD_INCORRECT").errorMessage("403_PASSWORD_INCORRECT").build());
                			response.getWriter().println(resultObj);
                			return;
            			}
                        
                        
                        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

                            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                                usernamePasswordAuthenticationToken
                                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                // After setting the Authentication in the context, we specify
                                // that the current user is authenticated. So it passes the
                                // Spring Security Configurations successfully.
                                //refresh 추가.
                                //System.out.println("##############"+SecurityContextHolder.getContext().getAuthentication().getName()); 
                              //refresh 추가.
                                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                                if(SecurityContextHolder.getContext().getAuthentication() != null) {
                                	System.out.println("##############"+SecurityContextHolder.getContext().getAuthentication().getName());
                                	String token = jwtTokenUtil.generateToken(userDetails);
                                	
                                	response.setHeader("refreshToken", token);
                                	System.out.println("############## refresh token setting"+token);
                                }else {
                                	System.out.println("##############"+"null!!");
                                }
                                //refresh 추가
                                chain.doFilter(request, response);
                            }else if(jwtTokenUtil.isTokenExpired(jwtToken)){
                            	response.setContentType(org.springframework.http.MediaType.APPLICATION_XML.toString());
                    			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    			String resultObj = gson.toJson(
                    					ErrorVO.builder().status(200).errorCode("403_EXPIREDJWT").errorMessage("JWT Token has expired").build());

                    			response.getWriter().println(resultObj);
                    			return;
                            }else {
                            	response.setContentType(org.springframework.http.MediaType.APPLICATION_XML.toString());
                    			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    			String resultObj = gson.toJson(
                    					ErrorVO.builder().status(200).errorCode("403_PASSWORD_INCORRECT").errorMessage("PASSWORD INCORRECT").build());

                    			response.getWriter().println(resultObj);
                    			return;
                            }
                        }else {
                        //운영 버전
                			response.setContentType(org.springframework.http.MediaType.APPLICATION_XML.toString());
                			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                			String resultObj = gson.toJson(
                					ErrorVO.builder().status(200).errorCode("403_PASSWORD_INCORRECT").errorMessage("PASSWORD INCORRECT").build());
                			response.getWriter().println(resultObj);
                			return;
                        }
                        
                        
                    } else {
                        logger.warn("JWT Token does not begin with Bearer String");
                       
                        request.setAttribute("exception", ErrorVO.builder().status(200).errorCode("UsernameFromTokenException").errorMessage("Username from token error").build());
            			response.setContentType(org.springframework.http.MediaType.APPLICATION_XML.toString());
            			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            			String resultObj = gson.toJson(
            					ErrorVO.builder().status(200).errorCode("403_ID_EMPTY").errorMessage("ID EMPTY").build());
            			response.getWriter().println(resultObj);
            			return;
                    	
                    }        	
                }
        	}
        	
        }
        

        
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