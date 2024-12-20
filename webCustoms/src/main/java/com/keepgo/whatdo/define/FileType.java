package com.keepgo.whatdo.define;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum FileType {

//	A(1,"인보이스/패킹",1),
	Z(0,"전체",1),
	A(1,"FTA CO",2),
	B(2,"O/BL",6), 
	C(3,"인증서류",8), 
	D(4,"화주서류",7),
	E(5,"RCEP CO",3),
	F(6,"APTA CO",4),
	G(7,"공장 CO",5),
	H(8,"정정인팩",9),
	
	K(10,"도장이미지",10),
	L(11,"CLP",11);
//	I(9,"화주서류",6);

	Integer id;
	String name;
	Integer order;
	private FileType(Integer id, String name, Integer order) {
		this.id = id;
		this.name = name;
		this.order = order;
	}
	
	public static List<FileTypeRes> getList(){
	
		List<FileTypeRes> l = new ArrayList<>();
		for(int i=0; i<FileType.values().length;i++) {
			
			
			l.add(
					FileTypeRes.builder()
					.id(FileType.values()[i].id)
					.name(FileType.values()[i].name)
					.sortOrder(FileType.values()[i].order).build()		
				)
			;
		}
		
		
		return l.stream()
				//도장이미지는 리스트에서 보이지 않음.
				.filter(t->{
					
					return (t.getId() != 10);
				})
				.sorted(Comparator.comparing(FileTypeRes::getSortOrder)).collect(Collectors.toList());
		
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
