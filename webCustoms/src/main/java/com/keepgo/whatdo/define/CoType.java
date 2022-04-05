package com.keepgo.whatdo.define;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum CoType {

	BL(0,"병합",0),
	A(1,"FTA",1),
	B(2,"YATAI",2),
	C(3,"공백",3);
	
	Integer id;
	String name;
	Integer order;
	private CoType(Integer id, String name, Integer order) {
		this.id = id;
		this.name = name;
		this.order = order;
	}
	
	public static List<CoTypeRes> getList(){
	
		List<CoTypeRes> l = new ArrayList<>();
		for(int i=0; i<CoType.values().length;i++) {
			
			
			l.add(
					CoTypeRes.builder()
					.id(CoType.values()[i].id)
					.name(CoType.values()[i].name)
					.sortOrder(CoType.values()[i].order).build()		
				)
			;
		}
		
		
		return l.stream().sorted(Comparator.comparing(CoTypeRes::getSortOrder)).collect(Collectors.toList());
		
	}
	
}
