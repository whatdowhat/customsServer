package com.keepgo.whatdo.secuirty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.keepgo.whatdo.entity.Auth;
import com.keepgo.whatdo.entity.Member;
import com.keepgo.whatdo.repository.MemberRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {


	@Autowired
	MemberRepository _member;
	
    @Autowired
    public PasswordEncoder passwordEncoder;
	
    @Value("${app.administrator}")
    private String ADMINISTRATOR;
    
    @Value("${app.administratorPassword}")
    private String ADMINISTRATORPASSWORD;
    
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	//administrator
        if (ADMINISTRATOR.equals(username)) {
        	
        	 List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
				grantedAuthorityList.add(new SimpleGrantedAuthority(Auth.SUPER.toString()));
        	
//            return new User("user_id", "$2a$10$m/enYHaLsCwH2dKMUAtQp.ksGOA6lq7Fd2pnMb4L.yT4GyeAPRPyS",
            return new User(ADMINISTRATOR, passwordEncoder.encode(ADMINISTRATORPASSWORD),
//            						    
//            		                    $2a$10$rF8bt78oxA6xnD7oBNo0dey/FUFuowf7b3fB5g5cRe2SWJBZA5AIW
            		grantedAuthorityList);
        } else {
        //not adminiatrator
        	Member member  = _member.findByMemberId(username);
        	
        	
            if(member == null) {
            	throw new UsernameNotFoundException("User not found with username: " + username);	
            }else {
            		String passwrod  = member.getMemberPassword();
            	   List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
					grantedAuthorityList.add(new SimpleGrantedAuthority(Auth.LD.toString()));
            	   return new User(username, passwrod,
            			
            			   grantedAuthorityList);
            }
            
        }
    }
}