package com.keepgo.whatdo.define;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum GubunType {

	A(1,"FCL",1),
	B(2,"LCL",2),
	C(3,"코로드",3);
	
	Integer id;
	String name;
	Integer order;
	private GubunType(Integer id, String name, Integer order) {
		this.id = id;
		this.name = name;
		this.order = order;
	}
	
	public static List<GubunTypeRes> getList(){
	
		List<GubunTypeRes> l = new ArrayList<>();
		for(int i=0; i<GubunType.values().length;i++) {
			
			
			l.add(
					GubunTypeRes.builder()
					.id(GubunType.values()[i].id)
					.name(GubunType.values()[i].name)
					.sortOrder(GubunType.values()[i].order).build()		
				)
			;
		}
		
		
		return l.stream().sorted(Comparator.comparing(GubunTypeRes::getSortOrder)).collect(Collectors.toList());
		
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
