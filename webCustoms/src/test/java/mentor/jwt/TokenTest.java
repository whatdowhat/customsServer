package mentor.jwt;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.keepgo.whatdo.controller.service.MailService;
import com.keepgo.whatdo.secuirty.JwtTokenUtil;

public class TokenTest {

	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
 
	@org.junit.Test
	public void test() {
		
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		
		String password  = bCryptPasswordEncoder.encode("administrator");
		System.out.println(password);
		
		JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
		System.out.println(jwtTokenUtil);
		
		
//		String s=Double.toString(d);
//		int a = s.length();
//		System. out. println(s);
//		System. out. println(a);
//		System.out.println(String.valueOf(d).replaceAll(".","") .substring(0, 9));
		
		
		
		
	}

}
