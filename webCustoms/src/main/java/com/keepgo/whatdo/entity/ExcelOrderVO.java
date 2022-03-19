package com.keepgo.whatdo.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExcelOrderVO {

	public ExcelOrderVO(){
		
	}
	
	public ExcelOrderVO(String fieldName,String title,int order){
		this.fieldName = fieldName;
		this.title = title;
		this.order = order;
		
	}
	private String fieldName;
	private String title;
	private int order;
}
