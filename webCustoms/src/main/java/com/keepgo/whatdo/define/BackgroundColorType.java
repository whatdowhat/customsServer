package com.keepgo.whatdo.define;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum BackgroundColorType {

	A(0,"WHITE","#FFF","",0),
	B(1,"SALGU","#FDE9D9","S",1);
	
	
	
	public Integer id;
	public String name;
	public String code;
	public String showName;
	public Integer order;
	private BackgroundColorType(Integer id, String name, String code, String showName,Integer order) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.showName = showName;
		this.order = order;
	}
	
	
	
	public static List<BackgroundColorTypeRes> getList(){
	
		List<BackgroundColorTypeRes> l = new ArrayList<>();
		for(int i=0; i<BackgroundColorType.values().length;i++) {
			
			
			l.add(
					BackgroundColorTypeRes.builder()
					.id(BackgroundColorType.values()[i].id)
					.name(BackgroundColorType.values()[i].name)
					.code(BackgroundColorType.values()[i].code)
					.showName(BackgroundColorType.values()[i].showName)
					.sortOrder(BackgroundColorType.values()[i].order).build()		
				)
			;
		}
		
		
		return l.stream().sorted(Comparator.comparing(BackgroundColorTypeRes::getSortOrder)).collect(Collectors.toList());
		
	}
	
}
