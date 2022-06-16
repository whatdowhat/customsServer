package com.keepgo.whatdo.entity.customs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
//@Table(name = "web_unbi")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@ToString
public class Cp {

	private int rowIndex;
	
	private String data01;
	private String data02;
	private String data03;
	private String data04;
	private String data05;
	
	private String column01;
	@Builder.Default
	private int column01Span=1;
	
	private String column02;
	@Builder.Default
	private int column02Span=1;
	
	private String column03;
	@Builder.Default
	private int column03Span=1;
	
	private String column04;
	@Builder.Default
	private int column04Span=1;
	
	private String column05;
	@Builder.Default
	private int column05Span=1;
	
	private String column06;
	@Builder.Default
	private int column06Span=1;
	
	private String column07;
	@Builder.Default
	private int column07Span=1;
	
	private String column08;
	@Builder.Default
	private int column08Span=1;
	


	
	
}
