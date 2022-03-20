package mentor;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Atomic {

	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@org.junit.Test
	public void test() {
		
		AtomicInteger atomicInteger = new AtomicInteger();
		
//		System.out.println(atomicInteger.getAndIncrement());
//		System.out.println(atomicInteger.getAndIncrement());
//		System.out.println(atomicInteger.getAndIncrement());
//		System.out.println(atomicInteger.getAndIncrement());
		
		Stream.of(1,2,3,4,5).forEach(t -> System.out.println(atomicInteger.getAndIncrement()));
		
		
	}

}
