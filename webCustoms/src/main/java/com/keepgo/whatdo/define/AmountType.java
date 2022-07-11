package com.keepgo.whatdo.define;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum AmountType {

//	BL(0,"",0),
	A(1,"PCS",1),
	B(2,"M",2),
	C(3,"YD",3),
	D(4,"KG",4),
	E(5,"SET",5),
	F(6,"DOZ",6),
	G(7,"PAIR",7);
	
	Integer id;
	String name;
	Integer order;
	private AmountType(Integer id, String name, Integer order) {
		this.id = id;
		this.name = name;
		this.order = order;
	}
	
	
	
	public static List<AmountTypeRes> getList(){
	
		List<AmountTypeRes> l = new ArrayList<>();
		for(int i=0; i<AmountType.values().length;i++) {
			
			
			l.add(
					AmountTypeRes.builder()
					.id(AmountType.values()[i].id)
					.name(AmountType.values()[i].name)
					.order(AmountType.values()[i].order).build()	
				)
			;
		}
		
		
		return l.stream().sorted(Comparator.comparing(AmountTypeRes::getOrder)).collect(Collectors.toList());
		
	}
	
}
