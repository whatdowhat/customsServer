package com.keepgo.whatdo.define;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum CorpType {

	A(1,"KMJ SHIPPING CO., LTD.","(주)케이엠제이해운","KMJP","KMJ SHIPPING & INVESTMENT", "RM547, 29, JUNGANG-RO, PYEONGTAEK-SI, GYEONGGI-DO, KOREA TEL:070-7467-9685 FAX:032-465-1638",1),
	B(2,"KMJ Sea & Air Co., Ltd","(주) 케이엠제이씨앤에어","KMJT","KMJ SEA & AIR CO.,LTD","(GUWOL-DONG, TECHNOVALLEY) RM 708~709, 1 SEONSUCHON  GONGWON-RO, 17BEON-GIL, NAMDONG-GU INCHEON KOREA TEL:+82-32-212-1636 FAX:+82-32-212-1637/232-0291",2);
	
	
	
	
	public Integer id;
	public String code;
	public String name; 
	String showName;
	String fullName;
	String address;
	Integer order;
	
	private CorpType(Integer id, String code, String name, String showName,String fullName,String address,Integer order) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.showName = showName;
		this.fullName = fullName;
		this.address = address;
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
					.showName(CorpType.values()[i].showName)
					.fullName(CorpType.values()[i].fullName)
					.address(CorpType.values()[i].address)
					.order(CorpType.values()[i].order).build()		
				)
			;
		}
		
		
		return l.stream().sorted(Comparator.comparing(CorpTypeRes::getOrder)).collect(Collectors.toList());
		
	}

	


	
	
	
}
