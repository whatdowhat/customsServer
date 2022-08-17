package com.keepgo.whatdo.define;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum DocumentType {

//	BL(0,"",0),
	A(1,"classpath:static"+File.separatorChar+"co"+File.separatorChar+"FTA.xlsx",1),
	B(2,"classpath:static"+File.separatorChar+"co"+File.separatorChar+"RCEP.xlsx",2),
	C(3,"classpath:static"+File.separatorChar+"co"+File.separatorChar+"YATAI.xlsx",3),
	D(4,"classpath:static"+File.separatorChar+"inpack"+File.separatorChar+"inpack.xlsx",4),
	E(5,"classpath:static"+File.separatorChar+"co"+File.separatorChar+"WIN-SABIS.xlsx",5),
	F(6,"classpath:static"+File.separatorChar+"inbound"+File.separatorChar+"INBOUND.xlsx",6),
	G(7,"classpath:static"+File.separatorChar+"co"+File.separatorChar+"CLP.xlsx",8),
	H(8," ",3),
	I(9,"classpath:static"+File.separatorChar+"inbound"+File.separatorChar+"PREVIEW.xlsx",9),
	J(10,"classpath:static"+File.separatorChar+"migration"+File.separatorChar+"COMMON.xlsx",8),
	K(11,"classpath:static"+File.separatorChar+"migration"+File.separatorChar+"PARTNER.xlsx",9),
	L(12,"classpath:static"+File.separatorChar+"co"+File.separatorChar+"COUNTDETAIL.xlsx",12);
	public Integer id;
	public String name;
	public Integer order;
	private DocumentType(Integer id, String name, Integer order) {
		this.id = id;
		this.name = name;
		this.order = order;
	}
	
	
	
	public static List<DocumentTypeRes> getList(){
	
		List<DocumentTypeRes> l = new ArrayList<>();
		for(int i=0; i<DocumentType.values().length;i++) {
			
			
			l.add(
					DocumentTypeRes.builder()
					.id(DocumentType.values()[i].id)
					.name(DocumentType.values()[i].name)
					.sortOrder(DocumentType.values()[i].order).build()		
				)
			;
		}
		
		
		return l.stream().sorted(Comparator.comparing(DocumentTypeRes::getSortOrder)).collect(Collectors.toList());
		
	}
	
}
