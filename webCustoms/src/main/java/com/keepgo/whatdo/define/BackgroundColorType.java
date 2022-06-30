package com.keepgo.whatdo.define;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum BackgroundColorType {

	A(0,"WHITE","#FFF","",255,255,255 ,0),
	B(1,"SALGU","#FDE9D9","S",253,233,217,1),
	C(2,"BLUE","#0099ff","B",0,153,255,2),
	D(3,"YELLOW","#ffffcc","Y",255,255,204,3);
	
	
	
	
	public Integer id;
	public String name;
	public String code;
	public String showName;
	public Integer r;
	public Integer g;
	public Integer b;
	public Integer order;
	private BackgroundColorType(Integer id, String name, String code, String showName,Integer r,Integer g,Integer b,Integer order) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.showName = showName;
		this.r=r;
		this.g=g;
		this.b=b;
		this.order = order;
		
	}
	
	
	
	public static List<BackgroundColorTypeRes> getList(){
	
		List<BackgroundColorTypeRes> l = new ArrayList<>();
		for(int i=0; i<2;i++) {
			
			
			l.add(
					BackgroundColorTypeRes.builder()
					.id(BackgroundColorType.values()[i].id)
					.name(BackgroundColorType.values()[i].name)
					.code(BackgroundColorType.values()[i].code)
					.showName(BackgroundColorType.values()[i].showName)
					.r(BackgroundColorType.values()[i].r)
					.g(BackgroundColorType.values()[i].g)
					.b(BackgroundColorType.values()[i].b)
					.sortOrder(BackgroundColorType.values()[i].order).build()		
				)
			;
		}
		
		
		return l.stream().sorted(Comparator.comparing(BackgroundColorTypeRes::getSortOrder)).collect(Collectors.toList());
		
	}
	
}
