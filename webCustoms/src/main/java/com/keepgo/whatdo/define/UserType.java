package com.keepgo.whatdo.define;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum UserType {

	SUPER(1,"SUPER","슈퍼",1),
	ADMIN(2,"ADMIN","관리자",2),
	INBOUND(3,"INBOUND","인바운드",3),
	USER(4,"USER","사용자",4);
	
	
	
	Integer id;
	String code;
	String name; ;
	Integer order;
	
	private UserType(Integer id, String code, String name, Integer order) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.order = order;
	}
	
	public static List<UserTypeRes> getList(){
	
		List<UserTypeRes> l = new ArrayList<>();
		for(int i=1; i<UserType.values().length;i++) {
			
			
			l.add(
					UserTypeRes.builder()
					.id(UserType.values()[i].id)
					.code(UserType.values()[i].code)
					.name(UserType.values()[i].name)
					.order(UserType.values()[i].order).build()		
				)
			;
		}
		
		
		return l.stream().sorted(Comparator.comparing(UserTypeRes::getOrder)).collect(Collectors.toList());
		
	}

	


	
	
	
}
