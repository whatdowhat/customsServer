package com.keepgo.whatdo.define;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum CorpType {

	A(1,"KMJ SHIPPING CO., LTD.","(주)케이엠제이해운",1),
	B(2,"KMJ Sea & Air Co., Ltd","(주) 케이엠제이씨앤에어",2);
	
	
	
	
	public Integer id;
	public String code;
	public String name; ;
	Integer order;
	
	private CorpType(Integer id, String code, String name, Integer order) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.order = order;
	}
	
	public static List<CorpTypeRes> getList(){
	
		List<CorpTypeRes> l = new ArrayList<>();
		for(int i=0; i<CorpType.values().length;i++) {
			
			
			l.add(
					CorpTypeRes.builder()
					.id(CorpType.values()[i].id)
					.code(CorpType.values()[i].code)
					.name(CorpType.values()[i].name)
					.order(CorpType.values()[i].order).build()		
				)
			;
		}
		
		
		return l.stream().sorted(Comparator.comparing(CorpTypeRes::getOrder)).collect(Collectors.toList());
		
	}

	


	
	
	
}
