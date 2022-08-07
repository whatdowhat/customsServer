package com.keepgo.whatdo.define;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum CurrencyType {

//	BL(0,"",0),
	A(1,"$",1),
	B(2,"￥",2);
	

	
	Integer id;
	String name;
	Integer order;
	private CurrencyType(Integer id, String name, Integer order) {
		this.id = id;
		this.name = name;
		this.order = order;
	}
	
	
	
	public static List<CurrencyTypeRes> getList(){
	
		List<CurrencyTypeRes> l = new ArrayList<>();
		for(int i=0; i<CurrencyType.values().length;i++) {
			
			
			l.add(
					CurrencyTypeRes.builder()
					.id(CurrencyType.values()[i].id)
					.name(CurrencyType.values()[i].name)
					.order(CurrencyType.values()[i].order).build()		
				)
			;
		}
		
		
		return l.stream().sorted(Comparator.comparing(CurrencyTypeRes::getOrder)).collect(Collectors.toList());
		
	}
	
}
