package com.keepgo.whatdo.define;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum FileType {

	A(1,"인보이스/패킹",1),
	B(2,"원산지증명서",2),
	C(3,"O/BL",8), 
	D(4,"인증서류",6), 
	E(5,"화주서류",4);

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
		
		
		return l.stream().sorted(Comparator.comparing(FileTypeRes::getSortOrder)).collect(Collectors.toList());
		
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
