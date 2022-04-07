package com.keepgo.whatdo.define;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum FreightType {

	A(1,"COLLECT",1),
	B(2,"PREPAID",2);
	
	
	Integer id;
	String name;
	Integer order;
	private FreightType(Integer id, String name, Integer order) {
		this.id = id;
		this.name = name;
		this.order = order;
	}
	
	public static List<FreightTypeRes> getList(){
	
		List<FreightTypeRes> l = new ArrayList<>();
		for(int i=0; i<FreightType.values().length;i++) {
			
			
			l.add(
					FreightTypeRes.builder()
					.id(FreightType.values()[i].id)
					.name(FreightType.values()[i].name)
					.sortOrder(FreightType.values()[i].order).build()		
				)
			;
		}
		
		
		return l.stream().sorted(Comparator.comparing(FreightTypeRes::getSortOrder)).collect(Collectors.toList());
		
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


	
	
	
}
