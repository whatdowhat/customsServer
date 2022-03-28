package mentor;

import java.util.stream.Stream;

import org.assertj.core.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.keepgo.whatdo.entity.customs.Common;

public class Test2 {

	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@org.junit.Test
	public void test() {
		

		Common c = new Common();
		
		
		Stream.of(Arrays.array(1,2,3,4)).forEach(item->{
			process01(c, item);
		});
		
		
		System.out.println(c);
		
		
	}
	
	
	public void process01 (Common c,int item) {
		c.setNm(c.getNm() + item);
	}

}
