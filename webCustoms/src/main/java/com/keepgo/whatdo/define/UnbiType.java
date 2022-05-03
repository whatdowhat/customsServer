package com.keepgo.whatdo.define;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum UnbiType {

	A(1,"운비",1,1),
	B(2,"균안-광주 운비",2,1),
	C(3,"픽업비",1,2),
	D(4,"광주-위해 운비",2,2),
	E(5,"상하차비",1,3),
	F(6,"픽업비",2,3),
	G(7,"화물회사-사무실",1,4),
	H(8,"상하차비",2,4),
	I(9,"검품비",3,4),
	J(10,"파렛작업비",4,4);
	
	Integer id;
	String name;
	Integer order;
	Integer group;
	
	private UnbiType(Integer id, String name, Integer order, Integer group) {
		this.id = id;
		this.name = name;
		this.order = order;
		this.group = group;
	}
	
	public static List<UnbiTypeRes> getList(){
	
		List<UnbiTypeRes> l = new ArrayList<>();
		for(int i=0; i<UnbiType.values().length;i++) {
			
			
			l.add(
					UnbiTypeRes.builder()
					.id(UnbiType.values()[i].id)
					.name(UnbiType.values()[i].name)
					.group(UnbiType.values()[i].group)
					.sortOrder(UnbiType.values()[i].order).build()		
				)
			;
		}
		
		
		return l.stream().sorted(Comparator.comparing(UnbiTypeRes::getSortOrder)).collect(Collectors.toList());
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}


	
	
	
}
