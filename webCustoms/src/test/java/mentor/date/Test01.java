package mentor.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;

public class Test01 {

	@Test
	public void test() throws ParseException {
		
		
		String date = "2022-05-09";
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
		
		
		String dateToStr = DateFormatUtils.format(simpleDateFormat.parse(date), "MMM, dd.yyyy");
		System.out.println("Date is "+ dateToStr);
		
		String date_in  = "2022-05-09";
		SimpleDateFormat fmt_in = new SimpleDateFormat ("yyyy-MM-dd");
		SimpleDateFormat fmt_out = new SimpleDateFormat ("MMM, dd.yyyy", Locale.ENGLISH);
		
		ParsePosition pos = new ParsePosition (0);
		java.util.Date outTime = fmt_in.parse (date_in, pos);
		String date_out = fmt_out.format (outTime);
		System.out.println(date_out);
		
		Stream.iterate(1, t->t+1).limit(10).sorted(Comparator.reverseOrder()).forEach(System.out::println);
		
		
	}

	public String getDateStr(String date_in) {
//		String date_in  = "2022-05-09";
		SimpleDateFormat fmt_in = new SimpleDateFormat ("yyyy-MM-dd");
		SimpleDateFormat fmt_out = new SimpleDateFormat ("MMM, dd.yyyy", Locale.ENGLISH);
		
		ParsePosition pos = new ParsePosition (0);
		java.util.Date outTime = fmt_in.parse (date_in, pos);
		String date_out = fmt_out.format (outTime);
		return date_out;
	}
	
}
