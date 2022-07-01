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
import com.keepgo.whatdo.exception.UsernameFromTokenException;
import com.keepgo.whatdo.secuirty.JwtTokenUtil;
import com.keepgo.whatdo.secuirty.JwtUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@Component
@CrossOrigin
public class JwtRequestFilter extends OncePerRequestFilter {

    @Value("${jwt.secretYn}")
    private String secretYn;
    
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

//    	chain.doFilter(request, response);
    	
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
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
                if(request.getRequestURI().equals("/authenticate")){
                	chain.doFilter(request, response);
                }else {
                    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                        try {
                        	jwtToken = requestTokenHeader.substring(7);
                            username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                        } catch (IllegalArgumentException e) {
                        	request.setAttribute("exception", ErrorVO.builder().status(200).errorCode("SignatureException").errorMessage("JWT validity cannot be asserted and should not be trusted").build());
                        	throw new UsernameFromTokenException("Username from token error");
                        } catch (ExpiredJwtException e) {
//                        	
                        	request.setAttribute("exception", ErrorVO.builder().status(200).errorCode("403_EXPIREDJWT").errorMessage("403_EXPIREDJWT").build());
                        	throw new UsernameFromTokenException("Username from token error");
                        	
                        } catch (Exception e) {
                        	
                        	request.setAttribute("exception", ErrorVO.builder().status(200).errorCode("SignatureException").errorMessage("JWT validity cannot be asserted and should not be trusted").build());
                        	throw new UsernameFromTokenException("Username from token error");
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
                                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                                chain.doFilter(request, response);
                            }else {
                            	System.out.println("token validation not pass!");
                            }
                        }else {
                        //운영 버전
                        	request.setAttribute("exception", ErrorVO.builder().status(200).errorCode("SignatureException").errorMessage("JWT validity cannot be asserted and should not be trusted").build());
                        	throw new UsernameFromTokenException("Username from token error");
                        }
                        
                        
                    } else {
                        logger.warn("JWT Token does not begin with Bearer String");
                        request.setAttribute("exception", ErrorVO.builder().status(200).errorCode("UsernameFromTokenException").errorMessage("Username from token error").build());
                        throw new UsernameFromTokenException("Username from token error");
                    	
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