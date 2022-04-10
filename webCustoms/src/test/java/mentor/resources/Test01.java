package mentor.resources;

import java.text.ParseException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class Test01 {

	@Autowired
	ResourceLoader resourceLoader;
	
	@Test
	public void test() throws ParseException {
		
		
		Resource resource = resourceLoader.getResource("classpath:file/hello.html"); 

		
		System.out.println(resource.exists());
//		resource.getFile();
//		resource.getURI();
		
		
	}

	
}
