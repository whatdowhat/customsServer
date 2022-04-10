package mentor.calculation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class Test01 {

	@Test
	public void test() {
		
		
		String boxsize ="44*31*30";
		
		String items[] = boxsize.split("\\*");
		List<Double> t= Stream.of(items)
				.map(item -> new Double("0."+item))
				.collect(Collectors.toList());
//				.forEach(System.out::println);
		System.out.println(t);
		Double r = t.stream().reduce( (a,b)->a*b).get();
		System.out.println(r*12);
		
		
		System.out.println(String.format("%.2f", r*12));
//		System.out.println(String.format("%.2f", 0.494));
		
//		t.get(0).
		
		
		
		
	}

}
