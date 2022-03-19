package mentor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.keepgo.whatdo.controller.service.MailService;

public class Test {

	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@org.junit.Test
	public void test() {
		
		
//		Double d = 1.01111223E9; 
//		           
//		PasswordEncoder a =passwordEncoder();
//		System.out.println(a.encode("user_pw"));
//		
//		
////		String s=Double.toString(d);
////		int a = s.length();
////		System. out. println(s);
////		System. out. println(a);
////		System.out.println(String.valueOf(d).replaceAll(".","") .substring(0, 9));
//		
//		try {
//			LocalDateTime st = LocalDateTime.ofInstant(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-03-14 23:59:59").toInstant(), ZoneId.systemDefault());
//			System.out.println(st);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		double a = 34.0;
		long b = (long) a;
		
		System. out. println(b);
		
	}

}
