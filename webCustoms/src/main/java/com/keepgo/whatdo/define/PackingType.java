package com.keepgo.whatdo.define;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum PackingType {

	A(1,"CTN","CARTON",1),
	B(2,"PL","PALLET",2),
	C(3,"GT","OTHER",3);
	
	
	
	Integer id;
	String code;
	String name; ;
	Integer order;
	
	private PackingType(Integer id, String code, String name, Integer order) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.order = order;
	}
	
	public static List<PackingTypeRes> getList(){
	
		List<PackingTypeRes> l = new ArrayList<>();
		for(int i=0; i<PackingType.values().length;i++) {
			
			
			l.add(
					PackingTypeRes.builder()
					.id(PackingType.values()[i].id)
					.code(PackingType.values()[i].code)
					.name(PackingType.values()[i].name)
					.order(PackingType.values()[i].order).build()		
				)
			;
		}
		
		
		return l.stream().sorted(Comparator.comparing(PackingTypeRes::getOrder)).collect(Collectors.toList());
		
	}

	


	
	
	
}
