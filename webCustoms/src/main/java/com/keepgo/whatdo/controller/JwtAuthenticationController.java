package com.keepgo.whatdo.controller;

import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.keepgo.whatdo.entity.ErrorVO;
import com.keepgo.whatdo.entity.JwtRequest;
import com.keepgo.whatdo.entity.JwtResponse;
import com.keepgo.whatdo.entity.Member;
import com.keepgo.whatdo.repository.MemberRepository;
import com.keepgo.whatdo.secuirty.JwtTokenUtil;
import com.keepgo.whatdo.secuirty.JwtUserDetailsService;

import io.jsonwebtoken.Claims;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


	@Autowired
	MemberRepository _member;
	
    @Autowired
    private JwtUserDetailsService userDetailsService;
    
    @Autowired
    public PasswordEncoder passwordEncoder;

//    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @RequestMapping(value = "/authenticate", method = {RequestMethod.POST,RequestMethod.GET})
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest,HttpServletRequest request) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword(),request);

        final UserDetails userDetails = userDetailsService
            .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);
        String ex =  jwtTokenUtil.getUsernameFromToken(token);
        Claims clam =  jwtTokenUtil.getAllClaimsFromToken(token);
        String claim = (String) clam.get("claims01");

        Date nn =  jwtTokenUtil.getExpirationDateFromToken(token);
        System.out.println(ex);
        System.out.println(claim);
        System.out.println(nn);
        
        return ResponseEntity.ok(new JwtResponse(token));
//        return ResponseEntity.ok("sdf");
    }

    @RequestMapping(value = "/authenticate/test", method = {RequestMethod.POST,RequestMethod.GET})
    public ResponseEntity<?> test(Principal principal) throws Exception {
    	System.out.println("principal :"+principal);
    	return ResponseEntity.ok("principal");
    }
    
    
    @RequestMapping(value = "/authenticate/user", method = {RequestMethod.POST,RequestMethod.GET})
    public String insertUser() throws Exception {
        //authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
    	System.out.println("/authenticate/user enter");
        Member b = Member.builder().memberId("asdf").memberPassword(passwordEncoder.encode("asdf")).build();
        _member.save(b);

        return "success";
    }
    
    private void authenticate(String username, String password, HttpServletRequest request) throws Exception {
    	
    	if(username.replaceAll(" ", "").equals("")) {
        	request.setAttribute("exception", "403_ID_EMPTY");
            throw new Exception("403_ID_EMPTY");
//            throw new Exception("USER_DISABLED");
    	}
    	
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
        	
        	request.setAttribute("exception", "403_PASSWORD_INCORRECT");
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}