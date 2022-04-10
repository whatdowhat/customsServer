package mentor.enmutest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;

import org.junit.Test;

import com.keepgo.whatdo.define.CoType;
import com.keepgo.whatdo.define.FileType;
import com.keepgo.whatdo.define.GubunType;
import com.keepgo.whatdo.define.GubunTypeRes;

public class Test1 {

	@Test
	public void test() {
		
		FileType.getList().forEach(System.out::println);
		CoType.getList().forEach(System.out::println);
		
		
		SimpleDateFormat foramt1 = new SimpleDateFormat("yyyy-mm-dd");
		try {
			System.out.println(foramt1.parse("2022-01-23"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		System.out.println(FileType.A.getId());
		
		
//		foramt1.parse("2022-01-23")
		
//		LocalDateTime.ofInstant(foramt1.parse("2022-01-23").toInstant(), ZoneId.systemDefault()).get
		
//		GubunType.getList().forEach(System.out::println);
//		
//		System.out.println(CoType.getIdBlank());
		
		
	}

}
