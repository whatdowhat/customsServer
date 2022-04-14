package com.keepgo.whatdo.define;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum ColorType {


	A(1,"BLUE","#0000FF","B",1),
	B(2,"RED","#FF0000","R",2),
	C(3,"GREEN","#00B050","G",3),
	D(4,"PINK","#FF00FF","PK",4),
	E(5,"ORANGE","#E26B0A","O",5),
	F(6,"PURPLE","#9900FF","PP",6),
	G(0,"BLACK","#333","",7);
	
	public Integer id;
	public String name;
	public String code;
	public String showName;
	public Integer order;
	private ColorType(Integer id, String name, String code, String showName,Integer order) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.showName = showName;
		this.order = order;
	}
	
	
	
	public static List<ColorTypeRes> getList(){
	
		List<ColorTypeRes> l = new ArrayList<>();
		for(int i=0; i<ColorType.values().length;i++) {
			
			
			l.add(
					ColorTypeRes.builder()
					.id(ColorType.values()[i].id)
					.name(ColorType.values()[i].name)
					.code(ColorType.values()[i].code)
					.showName(ColorType.values()[i].showName)
					.sortOrder(ColorType.values()[i].order).build()		
				)
			;
		}
		
		
		return l.stream().sorted(Comparator.comparing(ColorTypeRes::getSortOrder)).collect(Collectors.toList());
		
	}
	
}
